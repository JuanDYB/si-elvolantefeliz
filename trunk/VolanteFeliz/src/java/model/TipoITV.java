package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class TipoITV {
    private String codTipoITV;
    private String nombre;
    private int intervaloAno;
    private String observaciones;

    public TipoITV(String codTipoITV, String nombre, int intervaloAno, String observaciones) {
        this.codTipoITV = codTipoITV;
        this.nombre = nombre;
        this.intervaloAno = intervaloAno;
        this.observaciones = observaciones;
    }

    public String getCodTipoITV() {
        return codTipoITV;
    }

    public int getIntervaloAno() {
        return intervaloAno;
    }

    public String getNombre() {
        return nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }   
}
