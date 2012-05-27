package control.staf;

import com.sun.xml.wss.impl.policy.mls.EncryptionPolicy;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Alquiler;
import model.Incidencia;
import model.Vehiculo;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class ConfirmIncidenceServlet extends HttpServlet {

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
        if (this.validateForm(request)) {
            try {
                Incidencia inc = (Incidencia) request.getSession().getAttribute("incidenciaEnCurso");
                HashMap <String, Alquiler> alquileresPosibles = (HashMap <String, Alquiler>) request.getSession().getAttribute("posiblesAlquileres");
                request.getSession().removeAttribute("incidenciaEnCurso");
                String matricula = Tools.validateMatricula(request.getParameter("matricula"));
                String codAlquiler = request.getParameter("alquiler");
                Tools.validateUUID(codAlquiler);
                String newIncUUID = Tools.generaUUID();
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");

                Incidencia incidencia = new Incidencia(newIncUUID, inc.getTipoIncidencia(), codAlquiler, 
                        alquileresPosibles.get(codAlquiler).getCliente().getCodCliente(), inc.getFecha(), 
                        inc.getObservaciones(), inc.getPrecio());
                if (persistence.addInciencia(incidencia)){
                    request.setAttribute("resultados", "Incidencia añadida");
                    Tools.anadirMensaje(request, "Incidencia dada de alta correctamente", 'o');
                    request.getRequestDispatcher("/staf/manage_incidence.jsp").forward(request, response);
                    return;
                }else{
                    request.setAttribute("resultados", "Error añadiendo incidencia");
                    Tools.anadirMensaje(request, "Ocurrio un error añadiendo un incidencia", 'e');
                }
                
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validación de parámetros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario no válido");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'w');
        }
        request.getRequestDispatcher("/staf/newincidence.jsp").forward(request, response);
    }

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 3 && request.getParameter("matricula") != null
                && request.getParameter("alquiler") != null && request.getParameter("confirmInc") != null
                && request.getSession().getAttribute("incidenciaEnCurso") != null 
                && request.getSession().getAttribute("posiblesAlquileres") != null) {
            return true;
        }
        return false;
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
}
