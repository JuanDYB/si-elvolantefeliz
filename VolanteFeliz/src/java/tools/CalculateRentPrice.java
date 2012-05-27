package tools;

import java.math.BigDecimal;
import java.util.Date;
import model.Alquiler;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class CalculateRentPrice {
    private final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
    private Alquiler alq;
    private Date fechaEntrega;
    private Date fechaFin;
    private int combustibleFin;

    public CalculateRentPrice(Alquiler alq, Date fechaFin, Date fechaEntrega, int combustibleFin) {
        this.alq = alq;
        this.fechaFin = fechaFin;
        this.fechaEntrega = fechaEntrega;
        this.combustibleFin = combustibleFin;
    }
    
    
    public BigDecimal calculateRentPrice() {
        long diasAlquiler = this.difFechas(fechaFin, alq.getFechaInicio()) + 1;
        long diasExtra = this.difFechas(fechaEntrega, fechaFin);
        int combustibleGastado = alq.getVehiculo().getCapacidadCombustible() - combustibleFin;
        
        BigDecimal precioDiasNormal = alq.getTarifa().getPrecioDia().multiply(new BigDecimal(diasAlquiler));
        BigDecimal precioDiasExtra = alq.getTarifa().getPrecioDiaExtra().multiply(new BigDecimal(diasExtra));
        BigDecimal precioCombustible = alq.getTarifa().getPrecioCombustible().multiply(new BigDecimal(combustibleGastado));
        return alq.getTarifa().getPrecioBase().add(precioDiasNormal.add(precioDiasExtra.add(precioCombustible)));        
    }
    
    private long difFechas(Date actual, Date pasada) {
        return (actual.getTime() - pasada.getTime()) / MILLSECS_PER_DAY;
    }
    
}
