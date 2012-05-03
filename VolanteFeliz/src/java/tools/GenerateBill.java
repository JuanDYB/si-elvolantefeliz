package tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import model.Alquiler;
import model.Cliente;
import model.Factura;
import model.Incidencia;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class GenerateBill {

    Cliente cli;
    String[] alquileres;
    HashMap<String, Alquiler> alquileresFacturar;
    String[] incidencias;
    HashMap<String, Incidencia> incidenciasFacturar;
    HttpServletRequest request;
    BigDecimal precioFinal = BigDecimal.ZERO;
    BigDecimal precioFinalIVA = BigDecimal.ZERO;
    private final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
    
    public GenerateBill(Cliente cli, String[] alquileres, HashMap<String, Alquiler> alquileresFacturar, String[] incidencias, HashMap<String, Incidencia> incidenciasFacturar, HttpServletRequest request) {
        this.cli = cli;
        this.alquileres = alquileres;
        this.alquileresFacturar = alquileresFacturar;
        this.incidencias = incidencias;
        this.incidenciasFacturar = incidenciasFacturar;
        this.request = request;
    }
    
    public Factura generateBill() {
        WebConfig config = (WebConfig) request.getServletContext().getAttribute("appConfig");
        if (alquileres != null) {
            this.doLoopAlquileres();
        }
        if (incidencias != null) {
            this.doLoopIncidences();
        }
        if (alquileres != null || incidencias != null) {
            BigDecimal IVA = precioFinal.multiply(new BigDecimal(config.getIVA()).divide(new BigDecimal("100")));
            precioFinalIVA = precioFinal.add(IVA);
            Factura fact = new Factura(Tools.generaUUID(), cli, alquileresFacturar, incidenciasFacturar,
                    config.getIVA(), precioFinal, precioFinalIVA, Tools.getDate(), null, null, false);
            return fact;
        } else {
            return null;
        }
    }
    
    private void doLoopIncidences() {
        for (int i = 0; i < incidencias.length; i++) {
            Incidencia inc = incidenciasFacturar.get(incidencias[i]);
            if (inc == null) {
                request.setAttribute("resultados", "Componentes de factura eliminados");
                Tools.anadirMensaje(request, "Se ha eliminado la incidencia: " + incidencias[i] + " por resultar ya facturada", 'w');
            } else if (inc.getTipoIncidencia().isAbonaCliente()) {
                precioFinal = precioFinal.add(inc.getPrecio());
            }
        }
    }
    
    private void doLoopAlquileres() {
        for (int i = 0; i < alquileres.length; i++) {
            Alquiler alq = alquileresFacturar.get(alquileres[i]);
            if (alq == null) {
                request.setAttribute("resultados", "Componentes de factura eliminados");
                Tools.anadirMensaje(request, "Se ha eliminado el alquiler: " + alquileres[i] + " por resultar ya facturado", 'w');
            } else {
                precioFinal = precioFinal.add(alq.getPrecio());
//                precioFinal = precioFinal.add(this.calculateRentPrice(alq));
            }
        }
    }
    
    private BigDecimal calculateRentPrice(Alquiler alq) {
        long diasAlquiler = this.difFechas(alq.getFechaFin(), alq.getFechaInicio());
        long diasExtra = this.difFechas(alq.getFechaEntrega(), alq.getFechaFin());
        int combustibleGastado = alq.getVehiculo().getCapacidadCombustible() - alq.getCombustibleFin();
        
        BigDecimal precioDiasNormal = alq.getTarifa().getPrecioDia().multiply(new BigDecimal(diasAlquiler));
        BigDecimal precioDiasExtra = alq.getTarifa().getPrecioDiaExtra().multiply(new BigDecimal(diasExtra));
        BigDecimal precioCombustible = alq.getTarifa().getPrecioCombustible().multiply(new BigDecimal(combustibleGastado));
        return alq.getTarifa().getPrecioBase().add(precioDiasNormal.add(precioDiasExtra.add(precioCombustible)));        
    }
    
    private long difFechas(Date actual, Date pasada) {
        return (actual.getTime() - pasada.getTime()) / MILLSECS_PER_DAY;
        
    }    
}
