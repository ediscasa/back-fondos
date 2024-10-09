package co.com.fondos.model.fondosclientes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class FondosClientes {

    private String clienteId;
    private String fondoTransaccionId;
    private String nombreFondo;
    private String categoriaFondo;
    private Double montoMinimoFondo;
    private Double saldoFondoCliente;
    private Double saldoCliente;
    private String fechaTransaccion;
    private String tipoTransaccion;
    private String canalNotificacion;
    private String destinoNotificacion;
    private Boolean notificado;


}
