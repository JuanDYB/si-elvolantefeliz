package control.staf;

import control.Tools;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.errors.IntrusionException;
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
        if (validateForm(request)) {
            String codCliente = request.getParameter("codCliente");
            Tools.validateUUID(codCliente);
            PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
            boolean ok = persistence.deleteClient(codCliente);
            request.setAttribute("resultados", "Resultado operación");
            if (ok){
                Tools.anadirMensaje(request, "Cliente eliminado satisfactoriamente");
            }else{
                Tools.anadirMensaje(request, "Ocurrieron errores al eliminar el cliente");
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "Formulario recibido no esperado");
        }
        request.getRequestDispatcher("/staf/clientadministration.jsp").forward(request, response);
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
        if (request.getParameterMap().size() >= 1 && request.getParameter("codCliente") != null) {
            return true;
        }
        return false;
    }
}
