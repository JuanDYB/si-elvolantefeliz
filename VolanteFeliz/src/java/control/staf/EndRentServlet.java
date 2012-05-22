package control.staf;

import java.io.IOException;
import java.io.PrintWriter;
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
        if (this.validateForm(request)){
            try{
                Date fecha = new Date (Long.parseLong(request.getParameter("date")));
                int KMFin = Tools.validateNumber(request.getParameter("KMFin"), "Kilómetros", Integer.MAX_VALUE);
                int combustibleFin = Tools.validateNumber(request.getParameter("combustible_fin"), "Kilómetros", Integer.MAX_VALUE);
                String observaciones = Tools.getContentTextArea(request.getPart("observaciones"));
                Tools.validateUUID(request.getParameter("rent"));
                String codAlquiler = request.getParameter("rent");
                Tools.validateHTML(observaciones);
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Alquiler alq = persistence.getAlquiler(codAlquiler);
                
                if (alq != null && alq.getFechaEntrega() == null){
                    
                }else if (alq != null){
                    
                }else{
                    
                }
                
            }catch (IntrusionException ex){
                
            }catch (ValidationException ex){
                
            }
        }else{
            
        }
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
