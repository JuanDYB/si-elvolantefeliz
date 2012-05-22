package control.staf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cliente;
import model.Empleado;
import model.Sucursal;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class EditClientServlet extends HttpServlet {

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
                String codCliente = request.getParameter("codCliente");
                Empleado empl = (Empleado) request.getSession().getAttribute("empleado");
                Tools.validateUUID(codCliente);
                Cliente clienteOriginal = persistence.getClient(codCliente);
                Sucursal suc = persistence.getSucursal(empl.getCodSucursal());
                if (clienteOriginal == null) {
                    request.setAttribute("resultados", "Cliente no encontrado");
                    Tools.anadirMensaje(request, "El cliente que desea editar no se encuentra dado de alta en el sistema", 'w');
                    request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                }else if (!clienteOriginal.isActivo()){
                    request.setAttribute("resultados", "Cliente desactivado");
                    Tools.anadirMensaje(request, "El cliente que desea editar se ha dado de baja del sistema", 'w');
                    request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                }else if (clienteOriginal != null && !clienteOriginal.getCodSucursal().equals(suc.getCodSucursal()) && !suc.isCentral()) {
                    request.setAttribute("resultados", "Imposible editar cliente");
                    Tools.anadirMensaje(request, "No se puede editar el cliente seleccionado porque no pertenece a la sucursal actual", 'w');
                    request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                } else {
                    String name = Tools.validateName(request.getParameter("name"), 200, "Nombre Cliente", false);
                    String dir = Tools.validateAdress(request.getParameter("address"));
                    String phone = Tools.validatePhone(request.getParameter("phone"));
                    String company = Tools.validateName(request.getParameter("company"), 100, "Nombre Empresa", true);
                    String email = Tools.validateEmail(request.getParameter("email"));
                    int age = Tools.validateNumber(request.getParameter("age"), "Edad", 150);
                    Cliente clienteNuevo = new Cliente(codCliente, name, email, clienteOriginal.getDni(), dir, phone, company, clienteOriginal.getCodSucursal(), age, true);
                    boolean ok = persistence.editClient(codCliente, clienteNuevo);
                    request.setAttribute("resultados", "Resultados de la operaci&oacute;n");
                    if (ok){
                        Tools.anadirMensaje(request, "El cliente ha sido editado correctamente", 'o');
                        request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                    } else{
                        Tools.anadirMensaje(request, "Ha ocurrido un error editando el cliente", 'e');
                        request.getRequestDispatcher("/staf/editclient.jsp?cli=" + codCliente).forward(request, response);
                    }
                }
            } catch (IntrusionException ex){
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
                request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validación del formulario fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
                request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'w');
            request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
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
        if (request.getParameterMap().size() >= 8 && request.getParameter("codCliente") != null && request.getParameter("name") != null
                && request.getParameter("address") != null && request.getParameter("phone") != null && request.getParameter("company") != null
                && request.getParameter("email") != null && request.getParameter("age") != null && request.getParameter("send") != null) {
            return true;
        }
        return false;
    }
}
