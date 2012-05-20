package control.staf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cliente;
import model.Vehiculo;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewRentStage1Servlet extends HttpServlet {

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
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Tools.validateUUID(request.getParameter("cliente"));
                String codCliente = request.getParameter("cliente");
                String fechaInicio = request.getParameter("fechainicio");
                String fechaFin = request.getParameter("fechafin");
                Cliente cli = persistence.getClient(codCliente);
                if (cli != null) {
                    HashMap<String, Vehiculo> vehiculosDisponibles = persistence.getVehiclesForRent(codCliente, fechaInicio, fechaFin);
                    if (vehiculosDisponibles != null) {
                        request.setAttribute("clientRent", cli);
                        request.setAttribute("vehiclesRent", vehiculosDisponibles);
                        request.getRequestDispatcher("/staf/newrent.jsp?st=2").forward(request, response);
                        return;
                    } else {
                        request.setAttribute("resultados", "Vehiculos no disponibles");
                        Tools.anadirMensaje(request, "No se han encontrado vehículos disponibles para esas fechas", 'w');
                    }
                } else {
                    request.setAttribute("resultados", "Cliente no encontrado");
                    Tools.anadirMensaje(request, "El cliente seleccionado no ha sido encontrado", 'w');
                }
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validación fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario no esperadp");
            Tools.anadirMensaje(request, "El formualario recibido no se esperaba", 'w');
        }
        request.getRequestDispatcher("/staf/newrent.jsp?st=1").forward(request, response);
    }

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 4 && request.getParameter("cliente") != null && request.getParameter("fechainicio") != null
                && request.getParameter("fechafin") != null && request.getParameter("continue") != null) {
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
