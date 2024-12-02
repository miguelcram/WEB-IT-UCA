package es.uca.iw.webituca.Model;

public class Proyecto {
    private String nombre;
    private boolean activo;
    private boolean permisoGestion;

    //Constructor
    public Proyecto(String nombre, boolean activo, boolean permisoGestion) {
        this.nombre = nombre;
        this.activo = activo;
        this.permisoGestion = permisoGestion;
    }

    //GETTERS
    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isPermisoGestion() {
        return permisoGestion;
    }
}
