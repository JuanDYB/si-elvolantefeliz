package control.staf;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cliente;
import model.Empleado;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewRentStage2Servlet extends HttpServlet {

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
                Tools.validateUUID(request.getParameter("cli"));
                Tools.validateUUID(request.getParameter("vehiculo"));
                String codVehiculo = request.getParameter("vehiculo");
                String codCliente = request.getParameter("cli");
                Date fechaInicio = Tools.validateDate(request.getParameter("fechainicio"), "Fecha de inicio");
                Date fechaFin = Tools.validateDate(request.getParameter("fechafin"), "Fecha de fin");
                //------VALIDAR FECHAS-------//
                if (!Tools.compareDate(fechaInicio, fechaFin)){
                    request.setAttribute("resultados", "Fechas no válidas");
                    Tools.anadirMensaje(request, "Ha introducido una fecha de fin que no es mayor que la de inicio", 'w');
                    request.getRequestDispatcher("/staf/newrent.jsp?st=1").forward(request, response);
                    return;
                }
                //------VALIDAR FECHAS-------//
                String codTarifa = request.getParameter("tarifa");
                int KMInicio = Tools.validateNumber(request.getParameter("KMInicio"), "Kilómetros de inicio", Integer.MAX_VALUE);
                PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
                Cliente cli = persistence.getClient(codCliente);
                Empleado empl = (Empleado) request.getSession().getAttribute("empleado");
                if (cli != null && cli.isActivo()) {
                    if (cli.getCodSucursal().equals(empl.getCodSucursal())) {
                        boolean ok = persistence.newRent(request, empl.getCodSucursal(), codCliente, codVehiculo, fechaInicio, fechaFin, codTarifa, KMInicio);
                        if (ok) {
                            request.setAttribute("resultados", "Alquiler añadido correctamente");
                            Tools.anadirMensaje(request, "El alquiler ha sido dado de alta correctamente", 'o');
                            request.getRequestDispatcher("/staf/manage_rent.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("resultados", "No tiene permisos");
                        Tools.anadirMensaje(request, "No se puede escoger este cliente porque no pertenece a esta sucursal", 'w');
                    }
                } else if (!cli.isActivo()){
                    request.setAttribute("resultados", "Cliente no disponible");
                    Tools.anadirMensaje(request, "El cliente ha sido dado de baja del sistema", 'w');
                } else {
                    request.setAttribute("resultados", "Cliente no encontrado");
                    Tools.anadirMensaje(request, "El cliente seleccionado no ha sido encontrado", 'w');
                }

            } catch (IntrusionException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            } catch (ValidationException ex) {
                request.setAttribute("resultados", "Validación de parámetros fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        } else {
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formualario recibido no se esperaba", 'w');
        }
        request.getRequestDispatcher("/staf/newrent.jsp?st=1").forward(request, response);
    }

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 4 && request.getParameter("vehiculo") != null && request.getParameter("cli") != null
                && request.getParameter("KMInicio")!= null && request.getParameter("saveRent") != null) {
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
