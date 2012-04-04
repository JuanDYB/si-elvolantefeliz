package control;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import model.Empleado;
import model.Sucursal;
import persistence.PersistenceFactory;
import persistence.PersistenceInterface;

/**
 * Web application lifecycle listener.
 *
 * @author Juan Díez-Yanguas Barber
 */
public class StartListener implements ServletContextListener {
    
    private PersistenceInterface persistence;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        if (!this.validateStart(context)){
            throw new RuntimeException ("No se ha podido iniciar la aplicación, "
                    + "los parametros de contexto no son validos");
        }
        String persistenceMethod = context.getInitParameter("persistenceMethod");
        String recurso = context.getInitParameter("resourceName");
        String nameBD = context.getInitParameter("nameBD");
        
        persistence = PersistenceFactory.getInstance(persistenceMethod);
        boolean exito = persistence.init (recurso, nameBD);
        int numAdmin = persistence.numAdmin();
        if (numAdmin == 0){
            ConfigLoader config = new ConfigLoader("config.properties");
            HashMap <String,String> propInicio = config.getProperties();
            if (propInicio != null){
                String md5Pass = Tools.generateMD5Signature(propInicio.get("empl.Pass") + propInicio.get("empl.Pass").toUpperCase());
                if (md5Pass.equals("-1")){
                    throw new RuntimeException("No se ha encontrado el algoritmo MD5");
                }
                String codEmpl = Tools.generaUUID();
                String codSuc = Tools.generaUUID();
                
                Sucursal suc = new Sucursal (codSuc, propInicio.get("suc.Name"), propInicio.get("suc.Addr"), 
                        propInicio.get("suc.Tlf"), propInicio.get("suc.Fax"), Boolean.getBoolean(propInicio.get("suc.central")));
                
                Empleado empl = new Empleado (codEmpl, propInicio.get("empl.userName"), md5Pass, propInicio.get("empl.Name"), 
                        propInicio.get("empl.DNI"), propInicio.get("empl.Tlf"), propInicio.get("empl.Addr"), codSuc, 'a');
                
                Boolean addSuc = persistence.addSucursal(suc);
                if (addSuc == null || !addSuc && !persistence.addEmpleado(empl)){
                    throw new RuntimeException("Error introduciendo sucursal y empleado por defecto de inicio");
                }
            }else{
                throw new RuntimeException("Ocurrieron errores cargando propiedades de configuracion de inicio, la aplicacion no se inciara");
            }
        }else if (numAdmin == -1){
            throw new RuntimeException("No se pudo obtener numero de empleados con permisos de administracion, la aplicación no se iniciara");
        }  
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        boolean exito = persistence.exit();
        if (exito == false) {
            Logger.getLogger(StartListener.class.getName()).log(Level.SEVERE, "Ha habido errores cerrando la persistencia");
        }
    }
    
    private boolean validateStart (ServletContext context){
        boolean ok = false;
        if (context.getInitParameter("resourceName") != null && context.getInitParameter("nameBD") != null){
            if (!context.getInitParameter("resourceName").isEmpty() && !context.getInitParameter("nameBD").isEmpty()
                    && context.getInitParameter("persistenceMethod").equals("MySQL")){
                ok = true;
            }
        }
        return ok;
    }
}
