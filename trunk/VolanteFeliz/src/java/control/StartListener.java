package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import model.Empleado;
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
        
        if (persistence.numAdmin() <= 1){
            Empleado empl = new Empleado
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
        if (context.getInitParameter("resource") != null || context.getInitParameter("nameBD") != null){
            if (context.getInitParameter("persistenceMethod").equals("MySQL")) ok = true;
        }
        return ok;
    }
}
