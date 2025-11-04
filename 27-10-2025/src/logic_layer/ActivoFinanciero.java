package logic_layer;

import java.util.Random;

public class ActivoFinanciero {
    private String ticker; 
    private String nombre;
    private TipoMoneda moneda;   
    private double precio;

    public ActivoFinanciero(String ticker, String nombre, TipoMoneda moneda, double precioInicial) {
        this.ticker = ticker;
        this.nombre = nombre;
        this.moneda = moneda;
        this.precio = precioInicial;
    }

    public String getTicker() { return ticker; }
    public String getNombre() { return nombre; }
    public TipoMoneda getMoneda() { return moneda; }
    public double getPrecio() { return precio; }

    public void fluctuarPrecio(Random random) {
        double factor = 0.95 + (random.nextDouble() * 0.1);
        this.precio = this.precio * factor;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2f %s", 
            ticker, nombre, precio, moneda.getSimbolo());
    }
}
