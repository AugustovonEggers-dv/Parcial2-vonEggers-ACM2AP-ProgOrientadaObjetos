package logic_layer;

import java.util.HashMap;
import java.util.Map;

public class Portafolio {
    private Map<String, Integer> tenencias;

    public Portafolio() {
        this.tenencias = new HashMap<>();
    }

    public void agregarTenencia(String ticker, int cantidad) {
        int cantidadActual = tenencias.getOrDefault(ticker, 0);
        tenencias.put(ticker, cantidadActual + cantidad);
    }

    public RespuestaTransaccion quitarTenencia(String ticker, int cantidad) {
        int cantidadActual = tenencias.getOrDefault(ticker, 0);

        if (cantidad > cantidadActual) {
            return new RespuestaTransaccion(false, "No posee suficientes activos. Tenencia actual: " + cantidadActual);
        }
        
        tenencias.put(ticker, cantidadActual - cantidad);
        return new RespuestaTransaccion(true, "Venta registrada.");
    }
    
    public int getCantidad(String ticker) {
        return tenencias.getOrDefault(ticker, 0);
    }
    
    public Map<String, Integer> getTenencias() {
        return this.tenencias;
    }
}