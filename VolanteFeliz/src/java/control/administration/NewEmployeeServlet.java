package control.administration;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Empleado;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewEmployeeServlet extends HttpServlet {
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
                String codEmpleado = Tools.generaUUID();
                String nombre = Tools.validateName(request.getParameter("name"), 200, "Nombre del empleado", false);
                String userName = Tools.validateUserName(request.getParameter("userName"));
                String pass = Tools.validatePass(request.getParameter("pass"));
                String passRep = Tools.validatePass(request.getParameter("pass_rep"));
                String dni = Tools.validateDNI(request.getParameter("dni"));
                String tel = Tools.validatePhone(request.getParameter("phone"));
                String addr = Tools.validateAdress(request.getParameter("address"));
                Tools.validateUUID(request.getParameter("suc"));
                String codSucursal = request.getParameter("suc");
                char perm = Tools.validatePerm(request.getParameter("perm")).charAt(0);
                if (pass.equals(passRep)){
                    pass = Tools.generateMD5Signature(Tools.passForMD5(pass));
                    Empleado empl = new Empleado(codEmpleado, nombre, userName, pass, dni, tel, addr, codSucursal, perm);
                    boolean ok = persistence.addEmpleado(empl);
                    if (ok){
                        request.setAttribute("resultados", "Empleado añadido correctamente");
                        Tools.anadirMensaje(request, "Se ha dado de alta correctamente el nuevo empleado", 'o');
                        request.getRequestDispatcher("/staf/administration/manage_empl.jsp").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("resultados", "Error añadiendo empleado");
                        Tools.anadirMensaje(request, "Ha ocurrido un error dando de alta el empleado", 'e');
                    }
                }else{
                    request.setAttribute("resultados", "Parámetros fallidos");
                    Tools.anadirMensaje(request, "La repetición de la contraseña no coincide con la contraseña", 'w');
                }
            }catch (IntrusionException ex){
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }catch (ValidationException ex){
                request.setAttribute("resultados", "Validación de parámetros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        }else{
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "El formulario recibido no se esparaba", 'e');
        }
        request.getRequestDispatcher("/staf/administration/new_employee.jsp").forward(request, response);
    }
    
    private boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 10 && request.getParameter("name") != null && request.getParameter("userName") != null 
                && request.getParameter("pass") != null && request.getParameter("pass_rep") != null && request.getParameter("phone") != null 
                && request.getParameter("address") != null && request.getParameter("suc") != null && request.getParameter("perm") != null 
                && request.getParameter("send") != null && request.getParameter("dni") != null){
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
