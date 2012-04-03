package model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Factura {
    private String codFactura;
    private String codCliente;
    private ArrayList <String> codAlquiler;
    private ArrayList <String> codIncidencia;
    private int IVA;
    private double importeSinIVA;
    private double importe;
    private Date fechaEmision;
    private String formaPago;
    private Date fechaPago;
    private boolean pagado;

    public Factura(String codFactura, String codCliente, ArrayList<String> codAlquiler, ArrayList<String> codIncidencia, int IVA, double importeSinIVA, double importe, Date fechaEmision, String formaPago, Date fechaPago, boolean pagado) {
        this.codFactura = codFactura;
        this.codCliente = codCliente;
        this.codAlquiler = codAlquiler;
        this.codIncidencia = codIncidencia;
        this.IVA = IVA;
        this.importeSinIVA = importeSinIVA;
        this.importe = importe;
        this.fechaEmision = fechaEmision;
        this.formaPago = formaPago;
        this.fechaPago = fechaPago;
        this.pagado = pagado;
    }
}
