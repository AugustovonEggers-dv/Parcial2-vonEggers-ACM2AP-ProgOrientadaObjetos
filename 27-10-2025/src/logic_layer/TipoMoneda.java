package logic_layer;

public enum TipoMoneda {
    
    PESOS("ARS", 1.0),
    DOLARES("USD", 1000.0),
    EURO("EUR", 1200.0),
    REAL("BRL", 200.0);

    private final String simbolo;
    private final double tasaDeCambio;

    TipoMoneda(String simbolo, double tasaDeCambio) {
        this.simbolo = simbolo;
        this.tasaDeCambio = tasaDeCambio;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getTasaDeCambio() {
        return tasaDeCambio;
    }

    @Override
    public String toString() {
        return this.name() + " (" + this.simbolo + ")";
    }
}