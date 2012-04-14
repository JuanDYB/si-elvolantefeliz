package control.staf;

import tools.Tools;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Alquiler;
import model.Cliente;
import model.Incidencia;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class DeleteClientServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        if (validateForm(request)) {
            try {
                String codCliente = request.getParameter("cli");
                Tools.validateUUID(codCliente);
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Cliente cli = persistence.getClient(codCliente);
                if (cli != null) {
                    HashMap<String, Alquiler> alquileresPendientes = persistence.getAlquileresClienteSinFacturar(cli);
                    HashMap<String, Incidencia> incidenciasPendientes = persistence.getIncidenciasClienteSinFacturar(cli);
                    if (alquileresPendientes != null || incidenciasPendientes != null){
                        request.setAttribute("resultados", "Imposible borrar");
                        Tools.anadirMensaje(request, "El cliente seleccionado no se puede borrar porque tiene alquileres o incidencias pendientes de facturar", 'w');
                        request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                        return; 
                    }
                    boolean ok = persistence.deleteClient(codCliente);
                    request.setAttribute("resultados", "Resultado operación");
                    if (ok) {
                        Tools.anadirMensaje(request, "Cliente eliminado satisfactoriamente", 'o');
                    } else {
                        Tools.anadirMensaje(request, "Ocurrieron errores al eliminar el cliente", 'e');
                    }
                }
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validacion de parámetros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "Formulario recibido no esperado", 'w');
        }
        request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
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
        response.sendError(404);
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
        if (request.getParameterMap().size() >= 1 && request.getParameter("cli") != null) {
            return true;
        }
        return false;
    }
}
