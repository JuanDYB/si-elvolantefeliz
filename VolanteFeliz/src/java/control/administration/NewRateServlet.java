package control.administration;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Tarifa;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Lourdes Gómez Rincón
 */
public class NewRateServlet extends HttpServlet {

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
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                String nombre = Tools.validateName(request.getParameter("name"), 200, "Nombre de la tarifa", false);
                String descripcion = request.getParameter("marca");
                BigDecimal p_base = new BigDecimal (Tools.validatePrice(request.getParameter("modelo")));
                BigDecimal p_dia = new BigDecimal (Tools.validatePrice(request.getParameter("p_dia")));
                BigDecimal p_extra = new BigDecimal (Tools.validatePrice(request.getParameter("p_extra")));
                BigDecimal p_combustible = new BigDecimal (Tools.validatePrice(request.getParameter("p_combustible")));
                Tools.validateHTML(descripcion);
                String codTarifa = Tools.generaUUID();
                Tarifa tarifa = new Tarifa(codTarifa, nombre, descripcion.replaceAll("\n", "<br />"), p_base, p_dia, p_extra, p_combustible);
                
                boolean ok = persistence.addTarifa(tarifa);
                if (ok){
                    request.setAttribute("resultados", "Tarifa añadida");
                    Tools.anadirMensaje(request, "La tarifa ha sido añadida correctamente al sistema", 'o');
                    request.getRequestDispatcher("/staf/manage_rent.jsp").forward(request, response);
                    return;
                }
                request.setAttribute("resultados", "Tarifa no añadida");
                Tools.anadirMensaje(request, "Ha ocurrido un error dando de alta la tarifa en el sistema", 'e');
            }catch (IntrusionException ex){
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }catch (ValidationException ex){
                request.setAttribute("resultados", "Validación fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
            request.getRequestDispatcher("/staf/administration/new_rate.jsp").forward(request, response);
        }else{
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'w');
        }
    }
    
    private boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 7 && request.getParameter("nombre") != null && request.getParameter("descripcion") != null 
                && request.getParameter("p_base") != null && request.getParameter("p_dia") != null && request.getParameter("p_extra") != null 
                && request.getParameter("p_combustible") != null && request.getParameter("send") != null){
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
