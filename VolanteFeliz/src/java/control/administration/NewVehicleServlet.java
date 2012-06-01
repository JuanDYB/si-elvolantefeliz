package control.administration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Empleado;
import model.Vehiculo;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class NewVehicleServlet extends HttpServlet {

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
                String matricula = Tools.validateMatricula(request.getParameter("matricula"));
                String marca = Tools.validateMarca(request.getParameter("marca"));
                String modelo = Tools.validateModelo(request.getParameter("modelo"));
                String nBastidor = request.getParameter("nBastidor");
                int capCombustible = Tools.validateNumber(request.getParameter("combustible"), "Capacidad de combustible", Integer.MAX_VALUE);
                Tools.validateUUID(request.getParameter("tipoVehiculo"));
                Tools.validateUUID(request.getParameter("tipoITV"));
                Tools.validateUUID(request.getParameter("tipoRev"));
                
                String tipoVehiculo = request.getParameter("tipoVehiculo");
                String tipoITV = request.getParameter("tipoITV");
                String tipoRevision = request.getParameter("tipoRev");
                
                Empleado emplActivo = (Empleado)request.getSession().getAttribute("empleado");
                String codVehiculo = Tools.generaUUID();
                Vehiculo vechicle = new Vehiculo(codVehiculo, matricula, marca, modelo, nBastidor, 
                        capCombustible, emplActivo.getCodSucursal(), tipoVehiculo, tipoRevision, tipoITV);
                boolean ok = persistence.addVehiculo(vechicle);
                if (ok){
                    request.setAttribute("resultados", "Vehículo añadido correctamente");
                    Tools.anadirMensaje(request, "El vehículo ha sido dado de alta correctamente en el sistema", 'o');
                    request.getRequestDispatcher("/staf/manage_vehicles.jsp").forward(request, response);
                    return;
                }else{
                    request.setAttribute("resultados", "Ocurrio un error al añadir el vehículo");
                    Tools.anadirMensaje(request, "Ha ocurrido un error al añadir el nuevo vehículo", 'e');
                }
            }catch(IntrusionException ex){
                request.setAttribute("resultados", "Detectada una intrusión");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }catch(ValidationException ex){
                request.setAttribute("resultados", "Validación del formulario fallida");
                Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
            }
        }else{
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'w');
        }
        request.getRequestDispatcher("/staf/administration/new_vehicle.jsp").forward(request, response);
    }
    
    private boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 9 && request.getParameter("matricula") != null && request.getParameter("marca") != null 
                && request.getParameter("modelo") != null && request.getParameter("nBastidor") != null 
                && request.getParameter("tipoVehiculo") != null && request.getParameter("tipoITV") != null 
                && request.getParameter("tipoRev") != null && request.getParameter("send") != null 
                && request.getParameter("combustible") != null){
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
