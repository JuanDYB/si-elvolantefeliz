package control.staf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Empleado;
import model.Factura;
import model.Sucursal;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.GeneratePDFBill;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class GenerateBillServlet extends HttpServlet {

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
        if (validateForm(request)) {
            try {
                String[] alquileres = request.getParameterValues("alquileres");
                String[] incidencias = request.getParameterValues("incidencias");
                if (alquileres != null) {
                    for (int i = 0; i < alquileres.length; i++){
                        Tools.validateUUID(alquileres[i]);
                    }
                }
                if (incidencias != null) {
                    for (int i = 0; i < alquileres.length; i++){
                        Tools.validateUUID(incidencias[i]);
                    }
                }
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Factura factura = persistence.generarFactura(alquileres, incidencias);
                Sucursal suc = persistence.getSucursal(((Empleado)request.getSession().getAttribute("empleado")).getCodSucursal());
                GeneratePDFBill pdfBill = new GeneratePDFBill(factura, null, request.getServletContext().getRealPath("/"));
                pdfBill.generateBill();
                
                
                
            }catch(ValidationException ex){
                request.setAttribute("resultados", "Validacion de parametros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage());
            }
        } else {
        }
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
        if (request.getParameterMap().size() >= 2 && request.getParameter("genFact") != null
                && ((request.getParameter("alquiler") != null && request.getParameter("incidencia") != null)
                || (request.getParameter("alquiler") != null)
                || (request.getParameter("incidencia") != null))) {
            return true;

        }
        return false;
    }
}
