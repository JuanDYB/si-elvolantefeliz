package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Tarifa {
    private String codTarifa;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private double precioHora;
    private double precioExtra;

    public Tarifa(String codTarifa, String nombre, String descripcion, double precioBase, double precioHora, double precioExtra) {
        this.codTarifa = codTarifa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.precioHora = precioHora;
        this.precioExtra = precioExtra;
    }

    public String getCodTarifa() {
        return codTarifa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public double getPrecioExtra() {
        return precioExtra;
    }

    public double getPrecioHora() {
        return precioHora;
    }
}
