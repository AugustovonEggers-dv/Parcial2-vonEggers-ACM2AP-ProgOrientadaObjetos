package logic_layer;

public class CuentaCorriente extends Cuenta {
    
	private double limiteDescubierto;

   
    public CuentaCorriente(String titular, String numeroCuenta, TipoMoneda moneda, double limiteDescubierto) {
        
        super(titular, numeroCuenta, moneda); 
        this.limiteDescubierto = limiteDescubierto;
    }

    @Override
    public RespuestaTransaccion retirar(double monto, String concepto) {
        if (monto <= 0) {
            return new RespuestaTransaccion(false, "El monto a retirar debe ser positivo.");
        }

        double limiteReal = getSaldo() + this.limiteDescubierto; 
        if (limiteReal < monto) {
            return new RespuestaTransaccion(false, "Límite de descubierto excedido. Límite disponible: " + limiteReal);
        }
        
        setSaldo(getSaldo() - monto); 
        getMovimientos().add(new Movimiento(concepto, -monto, getMoneda()));
        return new RespuestaTransaccion(true, "Retiro exitoso. Nuevo saldo: " + getSaldo());
    }

	@Override
	public RespuestaTransaccion retirar1(double monto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespuestaTransaccion retirar(double monto) {
		// TODO Auto-generated method stub
		return null;
	}
}