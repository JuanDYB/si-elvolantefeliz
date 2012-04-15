package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Empleado;
import persistence.PersistenceInterface;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class StafFilter implements Filter {

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestMod = (HttpServletRequest) request;
        PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
        if (isPermited(requestMod.getSession(), persistence)){
            chain.doFilter(request, response);
        }else{
            requestMod.getSession().setAttribute("requestedPage", requestMod.getRequestURL().toString());
            requestMod.getRequestDispatcher("/WEB-INF/errorPage/restricted.jsp").forward(request, response);
        }



    }

    private boolean isPermited(HttpSession sesion, PersistenceInterface persistence) {
        if (sesion.getAttribute("login") != null && sesion.getAttribute("empleado") != null && (Boolean) sesion.getAttribute("login")) {
            Empleado empl = persistence.getEmployee("codEmpleado", ((Empleado) sesion.getAttribute("empleado")).getCodEmpleado());
            if (empl != null && (empl.getPermisos() == 'a' || empl.getPermisos() == 'e')) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
