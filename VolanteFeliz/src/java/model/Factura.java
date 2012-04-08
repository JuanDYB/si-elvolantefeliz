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
    private ArrayList <Alquiler> Alquileres;
    private ArrayList <Incidencia> Incidencias;
    private int IVA;
    private double importeSinIVA;
    private double importe;
    private String fechaEmision;
    private String formaPago;
    private String fechaPago;
    private boolean pagado;

    public Factura(String codFactura, String codCliente, ArrayList<Alquiler> codAlquiler, ArrayList<Incidencia> codIncidencia, int IVA, double importeSinIVA, double importe, String fechaEmision, String formaPago, String fechaPago, boolean pagado) {
        this.codFactura = codFactura;
        this.codCliente = codCliente;
        this.Alquileres = codAlquiler;
        this.Incidencias = codIncidencia;
        this.IVA = IVA;
        this.importeSinIVA = importeSinIVA;
        this.importe = importe;
        this.fechaEmision = fechaEmision;
        this.formaPago = formaPago;
        this.fechaPago = fechaPago;
        this.pagado = pagado;
    }

    public int getIVA() {
        return IVA;
    }

    public ArrayList<Alquiler> getAlquileres() {
        return Alquileres;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public String getCodFactura() {
        return codFactura;
    }

    public ArrayList<Incidencia> getIncidencias() {
        return Incidencias;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public double getImporte() {
        return importe;
    }

    public double getImporteSinIVA() {
        return importeSinIVA;
    }

    public boolean isPagado() {
        return pagado;
    }
    
    
}
