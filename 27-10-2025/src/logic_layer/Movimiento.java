package logic_layer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private LocalDateTime fechaHora;
    private String tipo; 
    private double monto; 
    private TipoMoneda moneda;

    public Movimiento(String tipo, double monto, TipoMoneda moneda) {
        this.fechaHora = LocalDateTime.now();
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = fechaHora.format(formato);
        
        String tipoMonto;
        double montoParaMostrar;

        if (monto > 0) {
            tipoMonto = "Entrada";
            montoParaMostrar = monto; 
        } else {
            tipoMonto = "Salida";
            montoParaMostrar = -monto; 
        }

        return String.format("[%s] %s | %s | %s %.2f | %s", 
            fechaFormateada, 
            tipo, 
            moneda, 
            tipoMonto, 
            montoParaMostrar,
            (monto > 0 ? "✅" : "❌") 
        );
    }
}
