package control.staf;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.*;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewIncidenceStage1Servlet extends HttpServlet {

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
                String codTipoIncidencia = request.getParameter("incType");
                Tools.validateUUID(codTipoIncidencia);
                String matricula = Tools.validateMatricula(request.getParameter("matricula"));
                Date fechaInc = Tools.validateDate(request.getParameter("fechainc"), "Fecha Incidencia");
                BigDecimal precio = new BigDecimal(Tools.validatePrice(request.getParameter("precio")));
                String observaciones = request.getParameter("observaciones").replaceAll("\n", "<br />");
                Tools.validateHTML(observaciones);
                
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Vehiculo vehiculo = persistence.getVehiculo("Matricula", matricula, null);
                if (vehiculo == null){
                    request.setAttribute("resultados", "Vehiculo no encontrado");
                    Tools.anadirMensaje(request, "No se ha encontrado el vehículo con la matrícula especificada", 'e');
                    request.getRequestDispatcher("/staf/newincidence.jsp").forward(request, response);
                    return;
                }
                
                TipoIncidencia tipoIncidencia = persistence.getTipoInciencia(codTipoIncidencia, null);
                if (tipoIncidencia == null){
                    request.setAttribute("resultados", "Tipo de incidencia no valido");
                    Tools.anadirMensaje(request, "No se ha encontrado el tipo de incidencia seleccionado", 'w');
                    request.getRequestDispatcher("/staf/newincidence.jsp").forward(request, response);
                    return;
                }
                
                HashMap <String, Alquiler> alquileresPosibles = persistence.getAlquileresPosiblesIncidencia(fechaInc, matricula, null, null);
                if (alquileresPosibles != null){
                    request.setAttribute("tipoInc", tipoIncidencia);
                    request.setAttribute("matricula", matricula);
                    request.setAttribute("fecha", fechaInc);
                    request.setAttribute("precio", precio);
                    request.setAttribute("observaciones", observaciones);
                    request.setAttribute("posiblesAlq", alquileresPosibles);
                    request.getRequestDispatcher("/staf/confirm_incidence.jsp").forward(request, response);
                    return;
                }else{
                    request.setAttribute("resultados", "Alquiler no encontrado");
                    Tools.anadirMensaje(request, "No se han encontrado alquileres coincidentes con la incidencia", 'w');
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
        if (request.getParameterMap().size() >= 6 && request.getParameter("fechainc") != null
                && request.getParameter("matricula") != null && request.getParameter("incType") != null
                && request.getParameter("precio") != null && request.getParameter("observaciones") != null
                && request.getParameter("newinc") != null) {
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
