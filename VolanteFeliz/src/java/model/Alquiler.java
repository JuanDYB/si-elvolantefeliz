package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Alquiler implements Serializable{
    private String codAlquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private Tarifa tarifa;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaEntrega;
    private BigDecimal precio;
    private int KMInicio;
    private int KMFin;
    private int combustibleFin;
    private String observaciones;

    public Alquiler(String codAlquiler, Cliente cliente, Vehiculo vehiculo, Tarifa tarifa, Date fechaInicio, Date fechaFin, Date fechaEntrega, BigDecimal precio, int KMInicio, int KMFin, int combustibleFin, String observaciones) {
        this.codAlquiler = codAlquiler;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.tarifa = tarifa;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaEntrega = fechaEntrega;
        this.precio = precio;
        this.KMInicio = KMInicio;
        this.KMFin = KMFin;
        this.combustibleFin = combustibleFin;
        this.observaciones = observaciones;
    }

    public int getKMFin() {
        return KMFin;
    }

    public int getKMInicio() {
        return KMInicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getCodAlquiler() {
        return codAlquiler;
    }

    public int getCombustibleFin() {
        return combustibleFin;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
}
