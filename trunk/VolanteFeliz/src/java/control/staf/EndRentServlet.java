package control.staf;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Alquiler;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class EndRentServlet extends HttpServlet {

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
                Date fecha = Tools.validateDate(request.getParameter("date"), "Fecha de Entrega");
                int KMFin = Tools.validateNumber(request.getParameter("KMFin"), "Kilómetros", Integer.MAX_VALUE);
                int combustibleFin = Tools.validateNumber(request.getParameter("combustible_fin"), "Kilómetros", Integer.MAX_VALUE);
                Tools.validateUUID(request.getParameter("rent"));
                String codAlquiler = request.getParameter("rent");
                String observaciones = null;
                if (request.getParameter("observaciones") != null && !request.getParameter("observaciones").isEmpty()) {
                    observaciones = request.getParameter("observaciones");
                    Tools.validateHTML(observaciones);
                }
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Alquiler alq = persistence.getAlquiler(codAlquiler);
                if (alq != null && alq.getFechaEntrega() == null) {
                    if (combustibleFin <= alq.getVehiculo().getCapacidadCombustible() && KMFin > alq.getKMInicio()) {
                        boolean ok = persistence.endRent(alq, fecha, KMFin, combustibleFin, observaciones);
                        if (ok) {
                            request.setAttribute("resultados", "Alquiler actualizado correctamente");
                            Tools.anadirMensaje(request, "Se ha finalizado correctamente el alquiler y el precio ha sido calculado", 'o');
                            request.getRequestDispatcher("/staf/viewrent.jsp?rent=" + codAlquiler).forward(request, response);
                            return;
                        } else {
                            request.setAttribute("resultados", "Alquiler no finalizado");
                            Tools.anadirMensaje(request, "Ocurrio un error finalizando el alquiler, disculpe las molestias", 'e');
                        }
                    } else if (combustibleFin <= alq.getVehiculo().getCapacidadCombustible()) {
                        request.setAttribute("resultados", "Datos incorrectos");
                        Tools.anadirMensaje(request, "Ha introducido un kilometraje no válido, ha de ser mayor que los kilómetros de inicio del alquiler", 'w');
                    } else {
                        request.setAttribute("resultados", "Datos incorrectos");
                        Tools.anadirMensaje(request, "Ha introducido una cantidad de combustible no válida para el vehículo de alquiler", 'w');
                    }
                } else if (alq != null) {
                    request.setAttribute("resultados", "Imposible finalizar");
                    Tools.anadirMensaje(request, "El alquiler ha sido finalizado previamente, no se puede continuar", 'w');
                } else {
                    request.setAttribute("resultados", "Alquiler no encontrado");
                    Tools.anadirMensaje(request, "El alquiler que desea finalizar no ha sido encontrado", 'e');
                }
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validacion de parametros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'e');
        }
        request.getRequestDispatcher("/staf/manage_rent.jsp").forward(request, response);
    }

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 5 && request.getParameter("KMFin") != null && request.getParameter("combustible_fin") != null
                && request.getParameter("date") != null && request.getParameter("endrent") != null) {
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
