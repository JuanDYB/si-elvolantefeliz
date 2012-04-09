package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Factura {
    private String codFactura;
    private Cliente codCliente;
    private ArrayList <Alquiler> Alquileres;
    private ArrayList <Incidencia> Incidencias;
    private int IVA;
    private BigDecimal importeSinIVA;
    private BigDecimal importe;
    private String fechaEmision;
    private String formaPago;
    private String fechaPago;
    private boolean pagado;

}
