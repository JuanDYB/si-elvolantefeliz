package model;

import java.util.Date;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Alquiler {
    private String codAlquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private Tarifa tarifa;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaEntrega;
    private double precio;
    private int KMInicio;
    private int KMFin;
    private int combustibleInicio;
    private int combustibleFin;
    private String observaciones;

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

    public int getCombustibleInicio() {
        return combustibleInicio;
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

    public double getPrecio() {
        return precio;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    
    
}
