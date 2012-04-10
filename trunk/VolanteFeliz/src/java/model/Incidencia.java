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
    private String codAlquiler;
    private String codCliente;
    private Date fecha;
    private String observaciones;
    private BigDecimal precio;

    public Incidencia(String codIncidencia, TipoIncidencia tipoIncidencia, String codAlquiler, String codCliente, Date fecha, String observaciones, BigDecimal precio) {
        this.codIncidencia = codIncidencia;
        this.tipoIncidencia = tipoIncidencia;
        this.codAlquiler = codAlquiler;
        this.codCliente = codCliente;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.precio = precio;
    }

    public String getCodAlquiler() {
        return codAlquiler;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public String getCodIncidencia() {
        return codIncidencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }
    
}
