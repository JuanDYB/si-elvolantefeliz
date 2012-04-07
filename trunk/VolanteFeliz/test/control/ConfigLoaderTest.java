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
        ConfigLoader instance = new ConfigLoader("/config.properties");
        HashMap<String, String> expResult = new HashMap<String, String>();
        expResult.put("suc.Name", "Defecto");
        expResult.put("suc.Addr", "Calle del pez, 1 28005-Madrid");
        expResult.put("suc.Tlf", "916683398");
        expResult.put("suc.Fax", "916683394");
        expResult.put("suc.central", "true");

        expResult.put("empl.Name", "Director");
        expResult.put("empl.userName", "admin");
        expResult.put("empl.pass", "admin");
        expResult.put("empl.DNI", "02324034h");
        expResult.put("empl.Addr", "Calle del pez, 1 28005-Madrid");
        expResult.put("empl.Tlf", "943228830");

        expResult.put("app.maxLoginAttempt", "10");
        
        HashMap result = instance.getProperties();
        assertEquals(expResult, result);
    }
}
