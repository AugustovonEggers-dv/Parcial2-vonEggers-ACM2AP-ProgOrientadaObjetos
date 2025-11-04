package logic_layer;

import java.util.ArrayList;
import java.util.List;

public abstract class Cuenta {
    private String titular;
    private String numeroCuenta;
    private double saldo;
    private TipoMoneda moneda;
    private List<Movimiento> movimientos;

    public Cuenta(String titular, String numeroCuenta, TipoMoneda moneda) {
    	
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.moneda = moneda;
        this.saldo = 0.0;
        this.movimientos = new ArrayList<>();
    }


    public double getSaldo() {
        return this.saldo;
    }

    public String getTitular() {
        return this.titular;
    }

    public void setSaldo(double nuevoSaldo) {
        this.saldo = nuevoSaldo;
    }
    
    public TipoMoneda getMoneda(){
    	return this.moneda;
    }


    public RespuestaTransaccion depositar(double monto, String string) {
        if (monto <= 0) {
            return new RespuestaTransaccion(false, "El monto a depositar debe ser positivo.");
        }
       
        this.saldo += monto; 
        this.movimientos.add(new Movimiento("Depósito", monto, this.moneda));
        return new RespuestaTransaccion(true, "Depósito exitoso. Nuevo saldo: " + this.saldo);
    }
    
    public abstract RespuestaTransaccion retirar1(double monto);
    

    public RespuestaTransaccion transferir(Cuenta cuentaDestino, double monto) {
        
        RespuestaTransaccion respuestaRetiro = this.retirar(monto, "Transferencia Saliente");

        if (!respuestaRetiro.esExitosa()) {
            return respuestaRetiro;
        }

        
        cuentaDestino.depositar(monto, "Transferencia Entrante");

        return new RespuestaTransaccion(true, "Transferencia exitosa.");
    }

    public abstract RespuestaTransaccion retirar(double monto);


    public List<Movimiento> getMovimientos() {
        return this.movimientos;
    }


	public abstract RespuestaTransaccion retirar(double monto, String concepto);


	public RespuestaTransaccion retirar1(double monto, String concepto) {
		// TODO Auto-generated method stub
		return null;
	}

 
    
}
