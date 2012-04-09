package model;

import java.math.BigDecimal;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Tarifa {
    private String codTarifa;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private BigDecimal precioDia;
    private BigDecimal precioDiaExtra;
    private BigDecimal precioCombustible;

    public Tarifa(String codTarifa, String nombre, String descripcion, BigDecimal precioBase, BigDecimal precioDia, BigDecimal precioDiaExtra, BigDecimal precioCombustible) {
        this.codTarifa = codTarifa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.precioDia = precioDia;
        this.precioDiaExtra = precioDiaExtra;
        this.precioCombustible = precioCombustible;
    }
    
    
}
