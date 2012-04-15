package control.staf;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cliente;
import model.Empleado;
import model.Factura;
import model.Sucursal;
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
        if (validateForm(request)) {
            PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
            Cliente client = persistence.getClient(request.getParameter("cliente"));
            if (client.getCodSucursal().equals(((Empleado) request.getSession().getAttribute("empleado")).getCodSucursal())) {
                try {
                    String[] alquileres = request.getParameterValues("alquileres");
                    String[] incidencias = request.getParameterValues("incidencias");
                    if (alquileres != null) {
                        if (client.getCompany() == null && alquileres.length > 1){
                            request.setAttribute("resultados", "Factura incorrecta");
                            Tools.anadirMensaje(request, "No se puede facturar más de un alquiler a un cliente particular", 'w');
                            request.getRequestDispatcher("/WEB-INF/errrorPage").forward(request, response);
                            return;
                        }
                        for (int i = 0; i < alquileres.length; i++) {
                            Tools.validateUUID(alquileres[i]);
                        }
                    }
                    if (incidencias != null) {
                        for (int i = 0; i < alquileres.length; i++) {
                            Tools.validateUUID(incidencias[i]);
                        }
                    }

                    Factura factura = persistence.generarFactura(client, alquileres, incidencias);
                    Sucursal suc = persistence.getSucursal(((Empleado) request.getSession().getAttribute("empleado")).getCodSucursal());
                    GeneratePDFBill pdfBill = new GeneratePDFBill(factura, suc, request.getServletContext().getRealPath("/"));
                    pdfBill.generateBill();
                    if (this.sendMail(request, client, factura)){
                        Tools.anadirMensaje(request, "Email de factura enviado correctamente", 'o');
                    }else{
                        Tools.anadirMensaje(request, "Ocurrio un error al enviar el email con la factura al cliente", 'w');
                    }



                } catch (ValidationException ex) {
                    request.setAttribute("resultados", "Validacion de parametros fallida");
                    Tools.anadirMensaje(request, ex.getUserMessage(), 'w');
                }
            } else {
                request.setAttribute("resultados", "Sucursal incorrecta");
                Tools.anadirMensaje(request, "Esta intentando generar la factura de un cliente que no pertenece a esta sucursal", 'w');
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
        if (request.getParameterMap().size() >= 3 && request.getParameter("genFact") != null && request.getParameter("cliente") != null
                && ((request.getParameter("alquiler") != null && request.getParameter("incidencia") != null)
                || (request.getParameter("alquiler") != null)
                || (request.getParameter("incidencia") != null))) {
            return true;

        }
        return false;
    }
    
    private boolean sendMail (HttpServletRequest request, Cliente client, Factura factura){
        String contenido = Tools.leerArchivoClassPath("/plantillaFactura.html");
        HashMap <String, String> adjuntos = new HashMap <String, String> ();
        adjuntos.put(request.getServletContext().getRealPath("/staf/billFolder/" + factura.getCodFactura() + ".pdf"), "Factura_" + factura.getCodFactura() + ".pdf");
        return Tools.emailSend(request, "El Volante Feliz: Factura", client.getEmail(), contenido, adjuntos);
    }
}