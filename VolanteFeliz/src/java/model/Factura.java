package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Factura {
    private String codFactura;
    private Cliente cliente;
    private HashMap <String, Alquiler> alquileres;
    private HashMap <String, Incidencia> incidencias;
    private int IVA;
    private BigDecimal importeSinIVA;
    private BigDecimal importe;
    private Date fechaEmision;
    private String formaPago;
    private Date fechaPago;
    private boolean pagado;

    public Factura(String codFactura, Cliente cliente, HashMap <String, Alquiler> alquileres, HashMap <String, Incidencia> incidencias, int IVA, BigDecimal importeSinIVA, BigDecimal importe, Date fechaEmision, String formaPago, Date fechaPago, boolean pagado) {
        this.codFactura = codFactura;
        this.cliente = cliente;
        this.alquileres = alquileres;
        this.incidencias = incidencias;
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

    public HashMap <String, Alquiler> getAlquileres() {
        return alquileres;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getCodFactura() {
        return codFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public BigDecimal getImporteSinIVA() {
        return importeSinIVA;
    }

    public HashMap <String, Incidencia> getIncidencias() {
        return incidencias;
    }

    public boolean isPagado() {
        return pagado;
    }
    
}
