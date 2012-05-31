package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class TipoVehiculo {
    private String codTipoVehiculo;
    private String nombre;
    private String fines;
    private int ocupantes;

    public TipoVehiculo(String codTipoVehiculo, String nombre, String fines, int ocupantes) {
        this.codTipoVehiculo = codTipoVehiculo;
        this.nombre = nombre;
        this.fines = fines;
        this.ocupantes = ocupantes;
    }

    public String getCodTipoVehiculo() {
        return codTipoVehiculo;
    }

    public String getFines() {
        return fines;
    }

    public String getNombre() {
        return nombre;
    }

    public int getOcupantes() {
        return ocupantes;
    }
    
    
}
