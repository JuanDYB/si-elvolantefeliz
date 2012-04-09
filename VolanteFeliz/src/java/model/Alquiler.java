package model;

import java.math.BigDecimal;
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
    
    
}
