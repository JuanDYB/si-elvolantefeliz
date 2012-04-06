package control.staf;

import control.Tools;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cliente;
import model.Empleado;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewClientServlet extends HttpServlet {
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
                String nombre = Tools.validateName(request.getParameter("name"), 200);
                String dni = Tools.validateDNI(request.getParameter("dni"));
                int edad = Tools.validateNumber(request.getParameter("age"), "Edad");
                String company = Tools.validateName(request.getParameter("company"), 100);
                String address = Tools.validateAdress(request.getParameter("address"));
                String tlf = Tools.validatePhone(request.getParameter("phone"));
                String email = Tools.validateEmail(request.getParameter("email"));
                String codSucursal = ((Empleado) request.getSession().getAttribute("empleado")).getCodSucursal();
                String codCliente = Tools.generaUUID();
                Cliente client = new Cliente(codCliente, nombre, email, dni, address, tlf, company, codSucursal, edad);
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                boolean ok = persistence.addClient(client);
                request.setAttribute("resultados", "Resultados de la operación");
                if (ok) {
                    Tools.anadirMensaje(request, "El cliente ha sido dado de alta correctamente");
                    request.getRequestDispatcher("/staf/index.jsp").forward(request, response);
                    return;
                } else {
                    Tools.anadirMensaje(request, "Ha ocurrido un error al dar de alta el nuevo cliente"); 
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
        request.getRequestDispatcher("/staf/newclient.jsp").forward(request, response);   
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
        if (request.getParameterMap().size() >= 8 && request.getParameter("dni") != null && request.getParameter("name") != null
                && request.getParameter("age") != null && request.getParameter("company") != null
                && request.getParameter("address") != null && request.getParameter("phone") != null && request.getParameter("email") != null
                && request.getParameter("send") != null) {
            return true;
        }
        return false;
    }
}
