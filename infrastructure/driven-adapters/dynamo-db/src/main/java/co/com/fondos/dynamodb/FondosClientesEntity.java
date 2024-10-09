package co.com.fondos.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class FondosClientesEntity {

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

    @DynamoDbPartitionKey
    @DynamoDbAttribute("cliente_id")  // Aquí mapeamos el nombre de la base de datos.
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("fondo_transaccion_id")
    public String getFondoTransaccionId() {
        return fondoTransaccionId;
    }

    public void setFondoTransaccionId(String fondoTransaccionId) {
        this.fondoTransaccionId = fondoTransaccionId;
    }

    @DynamoDbAttribute("nombre_fondo")
    public String getNombreFondo() {
        return nombreFondo;
    }

    public void setNombreFondo(String nombreFondo) {
        this.nombreFondo = nombreFondo;
    }

    @DynamoDbAttribute("categoria_fondo")
    public String getCategoriaFondo() {
        return categoriaFondo;
    }

    public void setCategoriaFondo(String categoriaFondo) {
        this.categoriaFondo = categoriaFondo;
    }

    @DynamoDbAttribute("monto_minimo_fondo")
    public Double getMontoMinimoFondo() {
        return montoMinimoFondo;
    }

    public void setMontoMinimoFondo(Double montoMinimoFondo) {
        this.montoMinimoFondo = montoMinimoFondo;
    }

    @DynamoDbAttribute("saldo_fondo_cliente")
    public Double getSaldoFondoCliente() {
        return saldoFondoCliente;
    }

    public void setSaldoFondoCliente(Double saldoFondoCliente) {
        this.saldoFondoCliente = saldoFondoCliente;
    }

    @DynamoDbAttribute("saldo_cliente")
    public Double getSaldoCliente() {
        return saldoCliente;
    }

    public void setSaldoCliente(Double saldoCliente) {
        this.saldoCliente = saldoCliente;
    }

    @DynamoDbAttribute("fecha_transaccion")
    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    @DynamoDbAttribute("tipo_transaccion")
    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    @DynamoDbAttribute("canal_notificacion")
    public String getCanalNotificacion() {
        return canalNotificacion;
    }

    public void setCanalNotificacion(String canalNotificacion) {
        this.canalNotificacion = canalNotificacion;
    }

    @DynamoDbAttribute("destino_notificacion")
    public String getDestinoNotificacion() {
        return destinoNotificacion;
    }

    public void setDestinoNotificacion(String destinoNotificacion) {
        this.destinoNotificacion = destinoNotificacion;
    }

    @DynamoDbAttribute("notificado")
    public Boolean getNotificado() {
        return notificado;
    }

    public void setNotificado(Boolean notificado) {
        this.notificado = notificado;
    }
}


