package logic_layer;

import java.util.ArrayList;
import java.util.List;

public abstract class Tarjeta {
    private String titular;
    private String numeroTarjeta; 
    private String fechaVencimiento; 
    private String cvv;
    private EstadoTarjeta estado;
    private TipoMoneda moneda;
    private List<Movimiento> movimientos; 

    public Tarjeta(String titular, String numeroTarjeta, String fechaVencimiento, String cvv, TipoMoneda moneda) {
        this.titular = titular;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
        this.moneda = moneda;
        this.estado = EstadoTarjeta.ACTIVA; 
        this.movimientos = new ArrayList<>();
    }

    
    public RespuestaTransaccion habilitar() {
        if (this.estado == EstadoTarjeta.DESACTIVADA) {
            return new RespuestaTransaccion(false, "No se puede habilitar una tarjeta desactivada permanentemente.");
        }
        this.estado = EstadoTarjeta.ACTIVA;
        return new RespuestaTransaccion(true, "Tarjeta habilitada.");
    }

    public RespuestaTransaccion congelar() {
        if (this.estado == EstadoTarjeta.DESACTIVADA) {
            return new RespuestaTransaccion(false, "La tarjeta ya está desactivada.");
        }
        this.estado = EstadoTarjeta.CONGELADA;
        return new RespuestaTransaccion(true, "Tarjeta congelada temporalmente.");
    }
    
    public RespuestaTransaccion desactivar() {
        this.estado = EstadoTarjeta.DESACTIVADA;
        return new RespuestaTransaccion(true, "Tarjeta dada de baja permanentemente.");
    }
    
    public EstadoTarjeta getEstado() { return this.estado; }
    public TipoMoneda getMoneda() { return this.moneda; }
    public String getTitular() { return this.titular; }
    public List<Movimiento> getMovimientos() { return this.movimientos; }

  
    public String getNumeroVisible() {
        return "**** **** **** " + this.numeroTarjeta.substring(this.numeroTarjeta.length() - 4);
    }
    
    
    public String getDatosCompletos() {
        return String.format("Número: %s\nVence: %s\nCVV: %s", this.numeroTarjeta, this.fechaVencimiento, this.cvv);
    }

    
    public abstract RespuestaTransaccion realizarPago(double monto, String concepto);
    
    protected abstract boolean puedeCambiarMoneda();
    
    public RespuestaTransaccion cambiarMoneda(TipoMoneda nuevaMoneda) {
        if (this.moneda == nuevaMoneda) {
            return new RespuestaTransaccion(false, "La tarjeta ya opera en " + nuevaMoneda.name());
        }

        if (!puedeCambiarMoneda()) {
            return new RespuestaTransaccion(false, "No se puede cambiar la moneda. El saldo/deuda debe ser cero, o la tarjeta no lo permite.");
        }
        
        this.moneda = nuevaMoneda;
        return new RespuestaTransaccion(true, "Moneda de la tarjeta actualizada a " + nuevaMoneda.name());
    }
}