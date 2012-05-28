/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author JuanDYB
 */
public class ToolsTest {

    @Test
    public void testGenerateMD5Signature() {
        System.out.println("generateMD5Signature");
        String input = "hola";
        String expResult = "4d186321c1a7f0f354b297e8914ab240";
        String result = Tools.generateMD5Signature(input);
        assertEquals(expResult, result);
    }


    @Test
    public void testValidateMatricula() throws Exception {
        System.out.println("validateMatricula");
        String input = "M-7545-ZH";
        String expResult = "M-7545-ZH";
        String result = Tools.validateMatricula(input);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidateEmail() throws Exception {
        System.out.println("validateEmail");
        String input = "hola@gmail.com";
        String expResult = "hola@gmail.com";
        String result = Tools.validateEmail(input);
        assertEquals(expResult, result);
        
    }

    @Test
    public void testValidateUserName() throws Exception {
        System.out.println("validateUserName");
        String input = "juan";
        String expResult = "juan";
        String result = Tools.validateUserName(input);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidateDNI() throws Exception {
        System.out.println("validateDNI");
        String input = "12345678Z";
        String expResult = "12345678Z";
        String result = Tools.validateDNI(input);
        assertEquals(expResult, result);

    }

    @Test
    public void testValidateHost() throws Exception {
        System.out.println("validateHost");
        String input = "mail.pepito.es";
        String expResult = "mail.pepito.es";
        String result = Tools.validateHost(input);
        assertEquals(expResult, result);
    }

    @Test
    public void testPrintBigDecimal() {
        System.out.println("printBigDecimal");
        BigDecimal input = new BigDecimal ("5.55");
        String expResult = "5,55";
        String result = Tools.printBigDecimal(input);
        assertEquals(expResult, result);
    }
}
