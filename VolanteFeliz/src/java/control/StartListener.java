package control;

import tools.MailSender;
import tools.WebConfig;
import tools.ConfigLoader;
import tools.Tools;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
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
        HashMap <String, String> config = this.cargaConfig();
        if (config == null){
            throw new RuntimeException("Ocurrieron errores cargando propiedades de configuracion de la aplicacion");
        }
        WebConfig appConfig = new WebConfig(config.get("app.maxLoginAttempt"), config.get("app.lockTime"), config.get("app.IVA"));
        String persistenceMethod = context.getInitParameter("persistenceMethod");
        String recurso = context.getInitParameter("resourceName");
        String nameBD = context.getInitParameter("nameBD");
        Tools.findAndCreateFolder(context.getRealPath("/staf/billFolder/"));
        Tools.findAndCreateFolder(context.getRealPath("/staf/vehicle_images/"));
        
        persistence = PersistenceFactory.getInstance(persistenceMethod);
        boolean exito = persistence.init (recurso, nameBD);
        int numAdmin = persistence.numAdmin();
        if (numAdmin == 0){
            
                String md5Pass = Tools.generateMD5Signature(Tools.passForMD5(config.get("empl.pass")));
                if (md5Pass.equals("-1")){
                    throw new RuntimeException("No se ha encontrado el algoritmo MD5");
                }
                String codEmpl = Tools.generaUUID();
                String codSuc = Tools.generaUUID();
                
                Sucursal suc = new Sucursal (codSuc, config.get("suc.Name"), config.get("suc.Addr"), 
                        config.get("suc.Tlf"), config.get("suc.Fax"), Boolean.getBoolean(config.get("suc.central")));
                
                Empleado empl = new Empleado (codEmpl, config.get("empl.Name"), config.get("empl.userName"), md5Pass 
                        ,config.get("empl.DNI"), config.get("empl.Tlf"), config.get("empl.Addr"), codSuc, 'a');
                
                Boolean addSuc = persistence.addSucursal(suc);
                if (addSuc == null || !addSuc || !persistence.addEmpleado(empl)){
                    throw new RuntimeException("Error introduciendo sucursal y empleado por defecto de inicio");
                }
            
        }else if (numAdmin == -1){
            throw new RuntimeException("No se pudo obtener numero de empleados con permisos de administracion, la aplicación no se iniciara");
        }
        MailSender mail = new MailSender(config);
        Authenticator autorizacionMail = mail.getAuth();
        sce.getServletContext().setAttribute("emailSender", mail);
        sce.getServletContext().setAttribute("mailAuth", autorizacionMail);

        sce.getServletContext().setAttribute("appConfig", appConfig);
        sce.getServletContext().setAttribute("persistence", persistence);
        
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
    
    private HashMap<String, String> cargaConfig (){
        ConfigLoader config = new ConfigLoader("config.properties");
        return config.getProperties();
    }
}
