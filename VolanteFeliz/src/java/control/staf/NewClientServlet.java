package control.staf;

import tools.Tools;
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
                String nombre = Tools.validateName(request.getParameter("name"), 200, "Nombre cliente", false);
                String dni = Tools.validateDNI(request.getParameter("dni"));
                int edad = Tools.validateNumber(request.getParameter("age"), "Edad", 150);
                String company = Tools.validateName(request.getParameter("company"), 100, "Nombre Empresa", true);
                String address = Tools.validateAdress(request.getParameter("address"));
                String tlf = Tools.validatePhone(request.getParameter("phone"));
                String email = Tools.validateEmail(request.getParameter("email"));
                String codSucursal = ((Empleado) request.getSession().getAttribute("empleado")).getCodSucursal();
                String codCliente = Tools.generaUUID();
                Cliente client = new Cliente(codCliente, nombre, email, dni, address, tlf, company, codSucursal, edad, true);
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Sucursal suc = persistence.getSucursal(codSucursal);
                request.setAttribute("resultados", "Resultados de la operación");
                if (suc == null){
                    Tools.anadirMensaje(request, "No se ha encontrado la sucursal que se desea asignar al cliente", 'e');
                    request.getRequestDispatcher("/staf/newclient.jsp").forward(request, response);
                    return;
                }
                boolean ok = persistence.addClient(client);
                if (ok) {
                    Tools.anadirMensaje(request, "El cliente ha sido dado de alta correctamente", 'o');
                    if (this.sendMail(request, client, suc)){
                        Tools.anadirMensaje(request, "Email de registro enviado correctamente", 'o');
                    }else{
                        Tools.anadirMensaje(request, "Ocurrió un error al mandar email de registro al cliente", 'w');
                    }
                    request.getRequestDispatcher("/staf/manageclients.jsp").forward(request, response);
                    return;
                } else {
                    Tools.anadirMensaje(request, "Ha ocurrido un error al dar de alta el nuevo cliente", 'e');
                }
            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Error en el formulario");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }            
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "No se esperaba este formulario", 'w');
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
    
    private boolean sendMail (HttpServletRequest request, Cliente client, Sucursal suc){
        String contenido = Tools.leerArchivoClassPath("/plantillaRegistroCliente.html");
        if (contenido == null){
            return false;
        }
        contenido.replace("&NAME_CLI&", client.getName());
        contenido.replace("&DNI_CLI&", client.getDni());
        contenido.replace("&DIR_CLI&", client.getAddress());
        contenido.replace("&TEL_CLI&", client.getTelephone());
        contenido.replace("&MAIL_CLI&", client.getEmail());
        if (client.getCompany() == null){
            contenido.replace("&COMPANY_CLI&", "Cliente Particular");
        }else{
            contenido.replace("&COMPANY_CLI&", client.getCompany());
        }
        contenido.replace("&AGE_CLI&", Integer.toString(client.getAge()));
        
        contenido.replace("&NAME_SUC&", suc.getNombre());
        contenido.replace("&DIR_SUC&", suc.getDir());
        contenido.replace("&TEL_SUC&", suc.getTelefono());
        contenido.replace("&FAX_SUC&", suc.getFax());
        
        return Tools.emailSend(request, "El Volante Feliz: Cliente registrado", client.getEmail(), contenido, null);
    }
}
