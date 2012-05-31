package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class TipoRevision {
    private String codTipoRevision;
    private int intervaloKM;
    private String nombre;

    public TipoRevision(String codTipoRevision, int intervaloKM, String nombre) {
        this.codTipoRevision = codTipoRevision;
        this.intervaloKM = intervaloKM;
        this.nombre = nombre;
    }

    public String getCodTipoRevision() {
        return codTipoRevision;
    }

    public int getIntervaloKM() {
        return intervaloKM;
    }

    public String getNombre() {
        return nombre;
    }
    
    
}
