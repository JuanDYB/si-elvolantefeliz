package control.administration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Empleado;
import model.Vehiculo;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import persistence.PersistenceInterface;
import tools.MultipartFormManage;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
@MultipartConfig
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
                String matricula = Tools.validateMatricula(MultipartFormManage.getcontentPartText(request.getPart("matricula")));
                String marca = Tools.validateMarca(MultipartFormManage.getcontentPartText(request.getPart("marca")));
                String modelo = Tools.validateModelo(MultipartFormManage.getcontentPartText(request.getPart("modelo")));
                String nBastidor = Tools.validateNBastidor(MultipartFormManage.getcontentPartText(request.getPart("nBastidor")));
                int capCombustible = Tools.validateNumber(MultipartFormManage.getcontentPartText(request.getPart("combustible")), "Capacidad de combustible", Integer.MAX_VALUE);
                
                String tipoVehiculo = MultipartFormManage.getcontentPartText(request.getPart("tipoVehiculo"));
                String tipoITV = MultipartFormManage.getcontentPartText(request.getPart("tipoITV"));
                String tipoRevision = MultipartFormManage.getcontentPartText(request.getPart("tipoRev"));
                
                Tools.validateUUID(tipoVehiculo);
                Tools.validateUUID(tipoITV);
                Tools.validateUUID(tipoRevision);
                
                String imageName = "";
                ////----Guardar Imagen si hay, si hay error guardando se aborta y notifica
                Part file = request.getPart("image_file");
                if (file != null && file.getSize() > 0){
                    imageName = Tools.validateFileName(MultipartFormManage.getFileNamePart(file.getHeader("content-disposition")));
                    if (!this.recuperarYGuardarImagenFormulario(file, request, imageName)){
                        return;
                    }
                }else if (request.getPart("image") != null){
                    imageName = Tools.validateFileName(MultipartFormManage.getcontentPartText(request.getPart("image")));
                }else{
                    request.setAttribute("resultados", "Imagen no seleccionada");
                    Tools.anadirMensaje(request, "Debe seleccionar o subir una imagen para el nuevo vehículo", 'w');
                    request.getRequestDispatcher("/staf/administration/new_vehicle.jsp").forward(request, response);
                    return;
                }
                ///-----Fin tratado de imagen
                
                
                
                Empleado emplActivo = (Empleado)request.getSession().getAttribute("empleado");
                String codVehiculo = Tools.generaUUID();
                Vehiculo vechicle = new Vehiculo(codVehiculo, matricula, marca, modelo, nBastidor, imageName, 
                        capCombustible, emplActivo.getCodSucursal(), tipoVehiculo, tipoRevision, tipoITV);
                boolean ok = persistence.addVehiculo(vechicle);
                if (ok){
                    request.setAttribute("resultados", "Vehículo añadido correctamente");
                    Tools.anadirMensaje(request, "El vehículo ha sido dado de alta correctamente en el sistema", 'o');
                    request.getRequestDispatcher("/staf/view_vehicle.jsp?v=" + codVehiculo).forward(request, response);
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
    
    private boolean validateForm (HttpServletRequest request) throws IOException, ServletException{
        if (request.getParameterMap().size() >= 9 && request.getPart("matricula") != null && request.getPart("marca") != null 
                && request.getPart("modelo") != null && request.getPart("nBastidor") != null 
                && request.getPart("tipoVehiculo") != null && request.getPart("tipoITV") != null 
                && request.getPart("tipoRev") != null && request.getPart("send") != null 
                && request.getPart("combustible") != null 
                && (request.getPart("image") != null || request.getPart("image_file") != null)){
            return true;
        }
        return false;
    }
    
    private boolean recuperarYGuardarImagenFormulario (Part file, HttpServletRequest request, String nombre) throws IOException, ServletException{
        if (file.getContentType().contains("image") == false || file.getSize() > 8388608) {
            request.setAttribute("resultados", "Archivo no válido");
            Tools.anadirMensaje(request, "Solo se admiten archivos de tipo imagen", 'w');
            Tools.anadirMensaje(request, "El tamaño máximo de archivo son 8 Mb", 'w');
            return false;
        } else {
            String fileName = request.getServletContext().getRealPath("/staf/vehicle_images/" + nombre);
            boolean ok = MultipartFormManage.guardarImagenEnElSistemaDeFicheros(file.getInputStream(), fileName);
            if (ok == false) {
                request.setAttribute("resultados", "Fallo al guardar archivo");
                Tools.anadirMensaje(request, "Ocurrio un error guardando la imagen", 'e');
                return false;
            }
        }
        return true;
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
