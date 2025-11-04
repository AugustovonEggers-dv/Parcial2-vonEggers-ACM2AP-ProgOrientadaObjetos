package logic_layer;

public class TarjetaDebito extends Tarjeta {

    private Cuenta cuentaAsociada;

    public TarjetaDebito(String titular, String numero, String venc, String cvv, TipoMoneda moneda, Cuenta cuentaAsociada) {
        super(titular, numero, venc, cvv, moneda);
        this.cuentaAsociada = cuentaAsociada;
    }

    @Override
    public RespuestaTransaccion realizarPago(double monto, String concepto) {
        if (getEstado() != EstadoTarjeta.ACTIVA) {
            return new RespuestaTransaccion(false, "La tarjeta no está activa (" + getEstado().name() + ").");
        }

        RespuestaTransaccion resp = cuentaAsociada.retirar(monto, concepto);
        
        return resp;
    }

	@Override
	protected boolean puedeCambiarMoneda() {
		return false;
	}
}