/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import model.Cliente;
import model.Factura;
import model.Sucursal;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author JuanDYB
 */
public class generatePDFBillTest {
    
    
    @Test
    public void testGenerateBill() {
        System.out.println("generateBill");
        Factura bill = null;
        Sucursal suc = new Sucursal("550e8400-e29b-41d4-a716-446655440000", "Central", "Calle del pez", "957752290", "952295620", true);
        Cliente cli = new Cliente("09e26881-a3c1-426f-a01d-39f8fd2dfde7", "Pepito", "pepito@gmail.com"
                , "05872056h", "Calle del pez", "9622834490", null, "550e8400-e29b-41d4-a716-446655440000", 25);
        generatePDFBill instance = new generatePDFBill(bill, suc, cli, "/Users/JuanDYB/Documents/Juan D-Y B/5 Informatica/SistemasInformaticos/CSIProyecto/VolanteFeliz/web");
        instance.generateBill();
    }
}
