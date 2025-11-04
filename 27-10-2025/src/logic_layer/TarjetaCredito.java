package logic_layer;

import java.util.Map;

public class TarjetaCredito extends Tarjeta {

    private double limiteCredito;
    private double saldoConsumido;
    private final double tasaInteresMensual;
    private final Map<Integer, Double> planesDeCuotas;
    
    public TarjetaCredito(String titular, String numero, String venc, String cvv, TipoMoneda moneda, 
            double limiteCredito, double tasaInteresMensual, Map<Integer, Double> planesDeCuotas) {
    	super(titular, numero, venc, cvv, moneda);
    	this.limiteCredito = limiteCredito;
    	this.saldoConsumido = 0.0;
    	this.tasaInteresMensual = tasaInteresMensual;
    	this.planesDeCuotas = planesDeCuotas; 
    }

    public double getLimiteCredito() { return this.limiteCredito; }
    public double getSaldoConsumido() { return this.saldoConsumido; }
    public double getDisponible() { return this.limiteCredito - this.saldoConsumido; }
    public Map<Integer, Double> getPlanesDeCuotas() { return this.planesDeCuotas; }

    @Override
    public RespuestaTransaccion realizarPago(double monto, String concepto) {
        if (getEstado() != EstadoTarjeta.ACTIVA) {
            return new RespuestaTransaccion(false, "La tarjeta no está activa (" + getEstado().name() + ").");
        }
        if (monto <= 0) {
            return new RespuestaTransaccion(false, "El monto debe ser positivo.");
        }
        if (getDisponible() < monto) {
            return new RespuestaTransaccion(false, "Límite de crédito insuficiente. Disponible: " + getDisponible());
        }

        this.saldoConsumido += monto;
        getMovimientos().add(new Movimiento(concepto, -monto, getMoneda()));
        
        return new RespuestaTransaccion(true, "Compra aprobada con T. de Crédito.");
    }
   
    public RespuestaTransaccion recibirPago(double monto) {
        if (monto <= 0) {
            return new RespuestaTransaccion(false, "El monto de pago debe ser positivo.");
        }
        this.saldoConsumido -= monto;
        getMovimientos().add(new Movimiento("Pago de Tarjeta", monto, getMoneda()));
        return new RespuestaTransaccion(true, "Pago aplicado. Saldo deudor: " + this.saldoConsumido);
    }
    @Override
    protected boolean puedeCambiarMoneda() {
        return this.saldoConsumido == 0.0;
    }
    public RespuestaTransaccion aplicarInteresFinanciacion() {
        if (this.saldoConsumido <= 0) {
            return new RespuestaTransaccion(false, "No hay saldo deudor para financiar.");
        }
        
        double interesGenerado = this.saldoConsumido * this.tasaInteresMensual;
        
        this.saldoConsumido += interesGenerado;

        getMovimientos().add(new Movimiento("Cargo por Interés", -interesGenerado, getMoneda()));

        return new RespuestaTransaccion(true, 
            String.format("Se aplicó un interés de $%.2f. Nueva deuda total: $%.2f",
                interesGenerado, this.saldoConsumido)
        );
    }
}