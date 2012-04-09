package model;

import java.math.BigDecimal;
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
    private BigDecimal precio;

    public Incidencia(String codIncidencia, TipoIncidencia tipoIncidencia, Date fecha, String observaciones, String codAlquiler, BigDecimal precio) {
        this.codIncidencia = codIncidencia;
        this.tipoIncidencia = tipoIncidencia;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.codAlquiler = codAlquiler;
        this.precio = precio;
    }

}
