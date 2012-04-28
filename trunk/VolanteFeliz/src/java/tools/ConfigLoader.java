package tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.owasp.esapi.errors.ValidationException;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class ConfigLoader {
    private String nombreFichero;
    private HashMap <String, String> propiedades = null;

    public ConfigLoader(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }
    
    private boolean loadFile(){
        InputStream is = null;
        Properties prop = null;
        boolean ok = false;
        try{
            is = ConfigLoader.class.getResourceAsStream("/" + this.nombreFichero);
            prop = new Properties();
            prop.load(is);
            Enumeration <String> propNames = (Enumeration <String>) prop.propertyNames();
            propiedades = new HashMap <String, String> ();
            while (propNames.hasMoreElements()){
                String key = propNames.nextElement();
                propiedades.put(key, prop.getProperty(key));
            }
            ok = true;
        }catch (IOException ex){
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, "Error cargando fichero de propiedades de configuracion", ex);
        }finally{
            try{
                is.close();
            }catch (IOException ex){
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, "Error cerrando recurso fichero propiedades", ex);
            }
        }
        return ok;
    }
    
    private boolean processSucursal (){
        if (propiedades.containsKey("suc.Name") && propiedades.containsKey("suc.Addr") && propiedades.containsKey("suc.Tlf") 
                && propiedades.containsKey("suc.Fax") && propiedades.containsKey("suc.central")){
            try{
                Tools.validateName(propiedades.get("suc.Name"), 100, "Nombre Sucursal", false);
                Tools.validateAdress(propiedades.get("suc.Addr"));
                Tools.validatePhone(propiedades.get("suc.Tlf"));
                Tools.validatePhone(propiedades.get("suc.Fax"));
                Tools.validateBool(propiedades.get("suc.central"));
                return true;
            }   catch (ValidationException ex){
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, ex.getLogMessage(), ex);
                return false;
            }         
        }
        return false;
    }
    
    private boolean processEmpl (){
        if (propiedades.containsKey("empl.Name") && propiedades.containsKey("empl.userName") && propiedades.containsKey("empl.pass") 
                && propiedades.containsKey("empl.DNI") && propiedades.containsKey("empl.Addr") && propiedades.containsKey("empl.Tlf")){
            try{
                Tools.validateName(propiedades.get("empl.Name"), 100, "Nombre Empleado", false);
                Tools.validateUserName(propiedades.get("empl.userName"));
                Tools.validatePass(propiedades.get("empl.pass"));
                Tools.validateDNI(propiedades.get("empl.DNI"));
                Tools.validateAdress(propiedades.get("empl.Addr"));
                Tools.validatePhone(propiedades.get("empl.Tlf"));
                return true;
            }   catch (ValidationException ex){
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, ex.getLogMessage(), ex);
                return false;
            }         
        }
        return false;
    }
    
    private boolean processAppOptions (){
        if (propiedades.containsKey("app.maxLoginAttempt") && propiedades.containsKey("app.lockTime") 
                && propiedades.containsKey("app.IVA")){
            try{
                Tools.validateNumber(propiedades.get("app.maxLoginAttempt"), "Maximo numero de intentos inicio sesion", Integer.MAX_VALUE);
                Tools.validateNumber(propiedades.get("app.lockTime"), "Tiempo de bloqueo de inicio sesion", Integer.MAX_VALUE);
                Tools.validateNumber(propiedades.get("app.IVA"), "IVA", Integer.MAX_VALUE);
                return true;
            }catch (ValidationException ex){
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, ex.getLogMessage(), ex);
                return false;
            }
        }
        return false;
    }
    
    private boolean processMailConfig(){
        if (propiedades.containsKey("smtp.host") && propiedades.containsKey("smtp.port") && propiedades.containsKey("smtp.user") 
                && propiedades.containsKey("smtp.tsl") && propiedades.containsKey("smtp.auth") 
                && propiedades.containsKey("smtp.pass") && propiedades.containsKey("smtp.from")){
            try{
                Tools.validateHost(propiedades.get("smtp.host"));
                Tools.validateNumber(propiedades.get("smtp.port"), "Puerto SMTP", Integer.MAX_VALUE);
                Tools.validateUserName(propiedades.get("smtp.user"));
                Tools.validateBool(propiedades.get("smtp.tsl"));
                Tools.validateBool(propiedades.get("smtp.auth"));
                Tools.validateEmail(propiedades.get("smtp.from"));
                return true;
            }catch (ValidationException ex){
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, ex.getLogMessage(), ex);
                return false;
            }
        }
        return false;
    }
    
    public HashMap <String, String> getProperties (){
        
        if (this.loadFile() && this.processEmpl() && this.processSucursal() && this.processAppOptions() && this.processMailConfig()){
            return propiedades;
        }
        return null;
    }
}
