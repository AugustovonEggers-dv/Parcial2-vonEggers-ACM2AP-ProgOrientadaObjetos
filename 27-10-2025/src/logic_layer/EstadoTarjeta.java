package logic_layer;

public enum EstadoTarjeta {
    ACTIVA("Habilitada para operar"),
    CONGELADA("Congelada temporalmente (se puede reactivar)"),
    DESACTIVADA("Baja permanente (no se puede reactivar)");

    private final String descripcion;

    EstadoTarjeta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
