package control;

import tools.WebConfig;
import tools.Tools;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Empleado;
import model.Sucursal;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class LoginServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(404);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("login") != null && (Boolean) request.getSession().getAttribute("login") 
                && request.getSession().getAttribute("empleado") != null){
            response.sendRedirect("/index.jsp");
            return;
        }
        WebConfig config = (WebConfig) request.getServletContext().getAttribute("appConfig");
        Integer intentos = (Integer) request.getSession().getAttribute("intentosLogin");
        if (intentos != null && intentos >= config.getMaxLoginAttempt()) {
            this.starTimer(request.getSession(), config.getLockTime());
            request.setAttribute("resultados", "Imposible inicio de sesion");
            Tools.anadirMensaje(request, "Intentos de inicio de sesión agotados", 'w');
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        if (validateForm(request)) {
            try {
                String userName = Tools.validateUserName(request.getParameter("userName"));
                String pass = Tools.validatePass(request.getParameter("pass"));
                String md5PassIntroducida = Tools.generateMD5Signature(Tools.passForMD5(pass));
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Empleado empl = persistence.getEmployee("UserName", userName);
                if (empl != null) {
                    Sucursal suc = persistence.getSucursal(empl.getCodSucursal());
                    if (suc == null){
                        request.setAttribute("resultados", "Sucursal desconocida");
                        Tools.anadirMensaje(request, "No se ha encontrado la sucursal asignada al empleado", 'e');
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    }
                    if (md5PassIntroducida.equals(empl.getPass())) {
                        request.getSession().setAttribute("login", true);
                        request.getSession().setAttribute("empleado", empl);
                        request.getSession().setAttribute("sucursal", suc);
                        
                        request.getSession().removeAttribute("intentosLogin");
                        if (request.getSession().getAttribute("requestedPage") != null){
                            String destination = (String) request.getSession().getAttribute("requestedPage");
                            request.getSession().removeAttribute("requestedPage");
                            response.sendRedirect(destination);
                            return;
                        }
                        if (empl.getPermisos() == 'a'){
                            response.sendRedirect("/staf/administration/index.jsp");
                        }else{
                            response.sendRedirect("/staf/index.jsp");
                        }
                        return;
                    } else {
                        request.setAttribute("resultados", "Usuario/contraseña desconocidos");
                        Tools.anadirMensaje(request, "Inicio de sesión fallido", 'w');
                    }
                } else {
                    request.setAttribute("resultados", "Usuario/contraseña desconocidos");
                    Tools.anadirMensaje(request, "Inicio de sesion fallido", 'w');
                }
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Error en el formulario");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "No se esperaba este formulario", 'w');
        }
        this.incrementarIntentos(request.getSession());
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 3 && request.getParameter("userName") != null && request.getParameter("pass") != null
                && request.getParameter("login") != null) {
            return true;
        }
        return false;
    }

    private void incrementarIntentos(HttpSession sesion) {
        if (sesion.getAttribute("intentosLogin") == null) {
            sesion.setAttribute("intentosLogin", 1);
        } else {
            sesion.setAttribute("intentosLogin", (Integer) sesion.getAttribute("intentosLogin") + 1);
        }
    }
    
    protected void starTimer(final HttpSession sesion, int time) {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                sesion.removeAttribute("intentosLogin");
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, time);
    }
}
