/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigDecimal;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author isen
 */
public class TarifaTest {
    Tarifa tarif = new Tarifa("123", "tarifEstandar", "descripcion", new BigDecimal("5255.25"),new BigDecimal("978.9"), new BigDecimal("787.88"), new BigDecimal("576.5525"));
  
    
    @Test
    public void testGetCodTarifa() {
        assertEquals("123", tarif.getCodTarifa());
    }

    @Test
    public void testGetDescripcion() {
    }

    @Test
    public void testGetNombre() {
    }

    @Test
    public void testGetPrecioBase() {
    }

    @Test
    public void testGetPrecioCombustible() {
    }

    @Test
    public void testGetPrecioDia() {
    }

    @Test
    public void testGetPrecioDiaExtra() {
    }
}
