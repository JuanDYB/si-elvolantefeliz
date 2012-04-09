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
    private ArrayList <Alquiler> alquileres;
    private ArrayList <Incidencia> incidencias;
    private int IVA;
    private BigDecimal importeSinIVA;
    private BigDecimal importe;
    private String fechaEmision;
    private String formaPago;
    private String fechaPago;
    private boolean pagado;

    public int getIVA() {
        return IVA;
    }

    public ArrayList<Alquiler> getAlquileres() {
        return alquileres;
    }

    public Cliente getCodCliente() {
        return codCliente;
    }

    public String getCodFactura() {
        return codFactura;
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

    public BigDecimal getImporte() {
        return importe;
    }

    public BigDecimal getImporteSinIVA() {
        return importeSinIVA;
    }

    public ArrayList<Incidencia> getIncidencias() {
        return incidencias;
    }

    public boolean isPagado() {
        return pagado;
    }
}
