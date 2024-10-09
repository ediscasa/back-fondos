package co.com.fondos.usecase.fondosclientes;

import co.com.fondos.model.fondosclientes.FondosClientes;
import co.com.fondos.model.fondosclientes.gateways.FondosClientesRepository;
import co.com.fondos.model.fondosclientes.gateways.NotificacionEmailService;
import co.com.fondos.model.fondosclientes.gateways.NotificacionSmsService;
import co.com.fondos.usecase.fondosclientes.exceptions.*;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class FondosClientesUseCase {

    public static final String TRANSACCION = "Transaccion#";
    public static final String FONDO = "Fondo#";
    public static final String UUUU_MM_DD_T_HH_MM_SS_SSS_Z = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String SMS = "sms";
    public static final String EMAIL = "email";

    private final FondosClientesRepository fondosClientesRepository;
    private final NotificacionSmsService notificacionSmsService;
    private final NotificacionEmailService notificacionEmailService;

    public FondosClientes suscribirFondo(FondosClientes model, Double saldoInicialCliente) {
        validarSuscripcion(model);
        Optional<FondosClientes> ultimaTransaccion = recuperarUltimaTransaccion(model.getClienteId());
        Double nuevoSaldoCliente = calcularNuevoSaldoClienteSuscribirFondo(ultimaTransaccion, model, saldoInicialCliente);

        model.setFondoTransaccionId(FONDO + model.getFondoTransaccionId());
        FondosClientes fondo = fondosClientesRepository.save(model);
        model.setNotificado(notificarSuscripcion(model));
        fondosClientesRepository.save(crearTransaccion(model, "Apertura",nuevoSaldoCliente));
        return fondo;
    }

    private void validarSuscripcion(FondosClientes model) {
        if (model.getSaldoFondoCliente() < model.getMontoMinimoFondo()) {
            throw new FundMinimumException("No cumple con el monto minimo para vincularse al fondo " + model.getNombreFondo());
        }
        Optional<FondosClientes> fondoPrevio = fondosClientesRepository.getEntityByKeys(model.getClienteId(), FONDO + model.getFondoTransaccionId());
        if (fondoPrevio.isPresent()) {
            throw new FundFoundException("El fondo " + fondoPrevio.get().getNombreFondo() + " ya existe para el cliente: " + fondoPrevio.get().getClienteId());
        }
    }

    private Boolean notificarSuscripcion(FondosClientes model) {
        if(model.getCanalNotificacion().equals(SMS)) {
            String messageId = notificacionSmsService.enviarSms("Se ha suscrito al fondo " + model.getNombreFondo(), model.getDestinoNotificacion());
            return messageId != null;
        } else if (model.getCanalNotificacion().equals(EMAIL)){
            String messageId = notificacionEmailService.enviarEmail("Se ha suscrito al fondo " + model.getNombreFondo(), model.getDestinoNotificacion());
            return messageId != null;
        } else {
            return false;
        }
    }

    public List<FondosClientes> obtenerFondosCliente(String clienteId) {
        return fondosClientesRepository.getEntityBeginsWith(clienteId, FONDO);
    }

    public void cancelarFondo(String clienteId, String fondoId) {
        Optional<FondosClientes> fondoARemover = fondosClientesRepository.getEntityByKeys(clienteId, FONDO + fondoId);
        if (fondoARemover.isPresent()) {
            Optional<FondosClientes> ultimaTransaccion = recuperarUltimaTransaccion(clienteId);
            Double nuevoSaldoCliente = calcularNuevoSaldoClienteCancelarFondo(ultimaTransaccion, fondoARemover.get());
            fondosClientesRepository.delete(fondoARemover.get());
            fondosClientesRepository.save(crearTransaccion(fondoARemover.get(), "Cancelacion", nuevoSaldoCliente));
        } else {
            throw new FundNotFoundException("El fondo con id: " + fondoId + " no existe para el cliente: " + clienteId);
        }
    }

    public List<FondosClientes> listarTransacciones(String clienteId) {
        return fondosClientesRepository.getEntityBeginsWith(clienteId, TRANSACCION);
    }

    private FondosClientes crearTransaccion(FondosClientes fondosClientes, String tipoTransaccion, Double saldoCliente) {
        ZonedDateTime fechaActual = ZonedDateTime.now(ZoneOffset.UTC);
        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern(UUUU_MM_DD_T_HH_MM_SS_SSS_Z));

        FondosClientes transaccion = FondosClientes.builder()
                .clienteId(fondosClientes.getClienteId())
                .fondoTransaccionId(TRANSACCION + fechaFormateada)
                .nombreFondo(fondosClientes.getNombreFondo())
                .categoriaFondo(fondosClientes.getCategoriaFondo())
                .montoMinimoFondo(fondosClientes.getMontoMinimoFondo())
                .saldoFondoCliente(fondosClientes.getSaldoFondoCliente())
                .saldoCliente(saldoCliente)
                .fechaTransaccion(fechaFormateada)
                .tipoTransaccion(tipoTransaccion)
                .canalNotificacion(fondosClientes.getCanalNotificacion())
                .destinoNotificacion(fondosClientes.getDestinoNotificacion())
                .notificado(fondosClientes.getNotificado())
                .build();
        return transaccion;
    }

    private Optional<FondosClientes> recuperarUltimaTransaccion(String clienteId) {
        return fondosClientesRepository.getLastEntityBeginsWith(clienteId, TRANSACCION);
    }

    private Double calcularNuevoSaldoClienteSuscribirFondo(Optional<FondosClientes> ultimaTransaccion, FondosClientes model, Double saldoInicialCliente) {
        Double nuevoSaldoCliente = 0d;
        Double saldoActualCliente = 0d;
        if (ultimaTransaccion.isPresent()) {
            saldoActualCliente = ultimaTransaccion.get().getSaldoCliente();
        } else {
            /**
             * Si no existe una transaccion en la base de datos signifca que es el primer fondo que se guardara para el cliente,
             * entonces se le asigna al cliente el saldo inicial configurado.
             */
            saldoActualCliente = saldoInicialCliente;
        }
        nuevoSaldoCliente = saldoActualCliente - model.getSaldoFondoCliente();
        if (nuevoSaldoCliente < 0) {
            System.out.println("No puedes crear un fondo de " + model.getSaldoFondoCliente() + " ya que solamente tienes  " + saldoActualCliente + " disponible.");
            throw new InsufficientFundsException("No tiene saldo disponible para vincularse al fondo " + model.getNombreFondo());
        } else {
            return nuevoSaldoCliente;
        }
    }

    private Double calcularNuevoSaldoClienteCancelarFondo(Optional<FondosClientes> ultimaTransaccion, FondosClientes model) {
        Double nuevoSaldoCliente = 0d;
        Double saldoActualCliente = 0d;
        if (ultimaTransaccion.isPresent()) {
            saldoActualCliente = ultimaTransaccion.get().getSaldoCliente();
        } else {
            /**
             * Si no existe una transaccion en la base de datos signifca que no se estan guardando las transacciones,
             * el sistema tiene error.
             */
            throw new TransactionNotFoundException("No hay transaccion relacionada a la creacion del fondo: " + model.getNombreFondo());
        }
        nuevoSaldoCliente = saldoActualCliente + model.getSaldoFondoCliente();
       return nuevoSaldoCliente;
    }

}
