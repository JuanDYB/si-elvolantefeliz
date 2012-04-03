package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Sucursal {
    private String codSucursal;
    private String nombre;
    private String dir;
    private String telefono;
    private String fax;
    private boolean central;

    public Sucursal(String codSucursal, String nombre, String dir, String telefono, String fax, boolean central) {
        this.codSucursal = codSucursal;
        this.nombre = nombre;
        this.dir = dir;
        this.telefono = telefono;
        this.fax = fax;
        this.central = central;
    }

    public boolean isCentral() {
        return central;
    }

    public String getCodSucursal() {
        return codSucursal;
    }

    public String getDir() {
        return dir;
    }

    public String getFax() {
        return fax;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }
    
}
