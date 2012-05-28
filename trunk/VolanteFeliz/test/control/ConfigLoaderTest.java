/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import tools.ConfigLoader;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author JuanDYB
 */
public class ConfigLoaderTest {

    /**
     * Test of getProperties method, of class ConfigLoader.
     */
    @Test
    public void testGetProperties() {
        System.out.println("getProperties");
        ConfigLoader instance = new ConfigLoader("config.properties");
        
        HashMap<String, String> expResult = new HashMap<String, String>();
        expResult.put("suc.Name", "Defecto");
        expResult.put("suc.Addr", "Calle del pez, 1 28005-Madrid");
        expResult.put("suc.Tlf", "916683398");
        expResult.put("suc.Fax", "916683394");
        expResult.put("suc.central", "true");

        expResult.put("empl.Name", "Director");
        expResult.put("empl.userName", "admin");
        expResult.put("empl.pass", "admin");
        expResult.put("empl.DNI", "02324034e");
        expResult.put("empl.Addr", "Calle del pez, 1 28005-Madrid");
        expResult.put("empl.Tlf", "943228830");

        expResult.put("app.maxLoginAttempt", "10");
        expResult.put("app.lockTime", "600000");
        expResult.put("app.IVA", "18");

        expResult.put("smtp.host", "mail.pepito.es");
        expResult.put("smtp.port", "26");
        expResult.put("smtp.tsl", "false");
        expResult.put("smtp.user", "volantefeliz+pepito.es");
        expResult.put("smtp.auth", "true");
        expResult.put("smtp.pass", "pepito");
        expResult.put("smtp.from", "pepito@pepito.es");


        HashMap result = instance.getProperties();
        assertEquals(expResult, result);
    }
}
