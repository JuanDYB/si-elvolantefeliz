package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Empleado;
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
        response.setContentType("text/html;charset=UTF-8");
        Integer intentos = (Integer) request.getSession().getAttribute("intentosLogin");
        int max = Integer.valueOf(((HashMap<String, String>) request.getServletContext().getAttribute("config")).get("app.maxLoginAttempt"));
        if (intentos != null && intentos <= max) {
            this.starTimer(request.getSession());
            request.setAttribute("resultados", "Imposible inicio de sesion");
            Tools.anadirMensaje(request, "Intentos de inicio de sesión agotados");
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
                    if (md5PassIntroducida.equals(empl.getPass())) {
                        request.getSession().setAttribute("login", true);
                        request.getSession().setAttribute("Empleado", empl);

                        request.getSession().removeAttribute("intentosLogin");
                        response.sendRedirect("/index.jsp");
                        return;
                    } else {
                        request.setAttribute("resultados", "Usuario/contraseña desconocidos");
                        Tools.anadirMensaje(request, "Inicio de sesion fallido");
                    }
                } else {
                    request.setAttribute("resultados", "Usuario/contraseña desconocidos");
                    Tools.anadirMensaje(request, "Inicio de sesion fallido");
                }
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage());
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Error en el formulario");
                Tools.anadirMensaje(request, ex.getUserMessage());
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "No se esperaba este formulario");
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
    
    protected void starTimer(final HttpSession sesion) {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                sesion.invalidate();
            }
        };

        Timer timer = new Timer();
        //10 minutos ---> 600.000 milisegundos
        timer.schedule(timerTask, 600000);
    }
}
