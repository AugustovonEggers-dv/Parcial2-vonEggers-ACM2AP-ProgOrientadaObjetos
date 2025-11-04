package logic_layer;

public class RespuestaTransaccion {
    private boolean exito;
    private String mensaje;

    public RespuestaTransaccion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }
    
    public boolean esExitosa() {
        return this.exito;
    }
    
    public String getMensaje() {
        return this.mensaje;
    }
}
