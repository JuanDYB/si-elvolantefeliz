package model;

import java.util.Date;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Incidencia {
    private String codIncidencia;
    private TipoIncidencia tipoIncidencia;
    private Date fecha;
    private String observaciones;
    private String codAlquiler;
    private double precio;

    public Incidencia(String codIncidencia, TipoIncidencia tipoIncidencia, Date fecha, String observaciones, String codAlquiler, double precio) {
        this.codIncidencia = codIncidencia;
        this.tipoIncidencia = tipoIncidencia;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.codAlquiler = codAlquiler;
        this.precio = precio;
    }
}
