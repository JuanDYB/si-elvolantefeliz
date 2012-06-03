package tools;

import java.math.BigDecimal;
import java.util.Date;
import model.Alquiler;
import model.Tarifa;
import model.Vehiculo;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class CalculateRentPriceTest {

    @Test
    public void testCalculateRentPrice() {
        //------DATOS NECESARIOS INICIALES-------------//
        Tarifa tarif = new Tarifa(null, null, null, new BigDecimal ("30.5"), new BigDecimal ("15.00"), 
                new BigDecimal ("35.5"), new BigDecimal ("1.55"));
        Date fInicio = Tools.stringToDate_ES("01-05-2012");
        Date fFin = Tools.stringToDate_ES("10-05-2012");
        Date fEntrega = Tools.stringToDate_ES("14-05-2012");
        Vehiculo ve = new Vehiculo(null, null, null, null, null, null, 90, null, null, null, null);
        Alquiler alq = new Alquiler(null, null, ve, tarif, fInicio, fFin, fEntrega, BigDecimal.ZERO, 0, 0, 0, null);
        
        //--------------TEST---------------//
        CalculateRentPrice calculator = new CalculateRentPrice(alq, fFin, fEntrega, 60);
        System.out.println("calculateRentPrice");
        BigDecimal expResult = new BigDecimal ("369.00");
        BigDecimal result = calculator.calculateRentPrice();
        assertEquals(expResult, result);
    }
}
