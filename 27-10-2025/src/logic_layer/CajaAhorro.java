package logic_layer;
import logic_layer.Cuenta;
public class CajaAhorro extends Cuenta {

	public CajaAhorro(String titular, String numeroCuenta, TipoMoneda moneda) {
      
        super(titular, numeroCuenta, moneda); 
    }

	@Override
	public RespuestaTransaccion retirar(double monto, String concepto) {
	    if (monto <= 0) {
	        return new RespuestaTransaccion(false, "El monto a retirar debe ser positivo.");
	    }

	    if (getSaldo() < monto) {
	        return new RespuestaTransaccion(false, "Fondos insuficientes. Saldo disponible: " + getSaldo());
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
