package co.com.fondos.api;
import co.com.fondos.model.fondosclientes.FondosClientes;
import co.com.fondos.usecase.fondosclientes.FondosClientesUseCase;
import co.com.fondos.usecase.fondosclientes.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/fondos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiRest {

    private final FondosClientesUseCase fondosClientesUseCase;
    private final Double saldoInicialCliente;

    public ApiRest(FondosClientesUseCase fondosClientesUseCase, @Value("${app.saldoInicialCliente}") Double saldoInicialCliente) {
        this.fondosClientesUseCase = fondosClientesUseCase;
        this.saldoInicialCliente = saldoInicialCliente;
    }

    @GetMapping(path = "/path")
    public String commandName() {
        return "Hello World";
    }

    @PostMapping("/suscribir")
    public ResponseEntity<String> suscribirFondo(@RequestBody FondosClientes request) {
        System.out.println("FondosClientes: " + request);
        FondosClientes fondoSuscrito = fondosClientesUseCase.suscribirFondo(request, saldoInicialCliente);
        return ResponseEntity.ok("Fondo suscrito exitosamente");
    }

    @DeleteMapping("/cancelar/{clienteId}/{fondoId}")
    public ResponseEntity<String> cancelarFondo(@PathVariable("clienteId") String clienteId, @PathVariable("fondoId") String fondoId) {
        fondosClientesUseCase.cancelarFondo(clienteId, fondoId);
        return ResponseEntity.ok("Fondo cancelado exitosamente");
    }

    @GetMapping("/cliente/{clienteId}/fondos")
    public ResponseEntity<List<FondosClientes>> obtenerFondosCliente(@PathVariable("clienteId") String clienteId) {
        List<FondosClientes> fondos = fondosClientesUseCase.obtenerFondosCliente(clienteId);
        return ResponseEntity.ok(fondos);
    }

    @GetMapping("/cliente/{clienteId}/transacciones")
    public ResponseEntity<List<FondosClientes>> listarTransaccionesCliente(@PathVariable("clienteId") String clienteId) {

        List<FondosClientes> transacciones = fondosClientesUseCase.listarTransacciones(clienteId);
        return ResponseEntity.ok(transacciones);
    }

    @ExceptionHandler({
            FundNotFoundException.class,
            InsufficientFundsException.class,
            TransactionNotFoundException.class,
            FundFoundException.class,
            FundMinimumException.class
    })
    public ResponseEntity<String> handleMultipleExceptions(RuntimeException ex) {
        return ResponseEntity.unprocessableEntity().body(ex.getMessage());
    }
}
