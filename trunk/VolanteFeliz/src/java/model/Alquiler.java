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
    
}
