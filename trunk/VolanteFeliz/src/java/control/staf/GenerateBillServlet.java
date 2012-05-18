package control.staf;

import java.io.IOException;
import java.math.BigDecimal;
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
            Empleado emplLogedIn = (Empleado) request.getSession().getAttribute("empleado");
            Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
            if (client.getCodSucursal().equals(emplLogedIn.getCodSucursal()) || suc.isCentral()) {
                try {
                    String[] alquileres = request.getParameterValues("alquiler");
                    String[] incidencias = request.getParameterValues("incidencia");
                    if (alquileres == null && incidencias == null) {
                        request.setAttribute("resultados", "Factura vacia");
                        Tools.anadirMensaje(request, "No se puede generar la factura por no haber ni alquileres ni incidencias por facturar", 'w');
                        request.getRequestDispatcher("/staf/bill_management.jsp").forward(request, response);
                        return;
                    }
                    if (alquileres != null) {
                        if (client.getCompany() == null && alquileres.length > 1) {
                            request.setAttribute("resultados", "Factura incorrecta");
                            Tools.anadirMensaje(request, "No se puede facturar más de un alquiler a un cliente particular", 'w');
                            request.getRequestDispatcher("/staf/bill_management.jsp").forward(request, response);
                            return;
                        }
                        for (int i = 0; i < alquileres.length; i++) {
                            Tools.validateUUID(alquileres[i]);
                        }
                    }
                    if (incidencias != null) {
                        for (int i = 0; i < incidencias.length; i++) {
                            Tools.validateUUID(incidencias[i]);
                        }
                    }

                    Factura factura = persistence.generarFactura(suc, client, alquileres, incidencias, request);
                    if (factura != null) {
                        request.setAttribute("resultados", "Resultados de la operación");
                        Tools.anadirMensaje(request, "Factura generada correctamente", 'o');
                        if (this.sendMail(request, factura, suc)) {
                            Tools.anadirMensaje(request, "Email de factura enviado correctamente", 'o');
                        } else {
                            Tools.anadirMensaje(request, "Ocurrio un error al enviar el email con la factura al cliente", 'w');
                        }
                        request.getRequestDispatcher("/staf/viewbill.jsp?bill=" + factura.getCodFactura()).forward(request, response);
                        return;
                    } else{
                        request.setAttribute("resultados", "Factura no generada");
                        Tools.anadirMensaje(request, "Ha ocurrido un error generando la factura", 'e');
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
            request.setAttribute("resultados", "Formulario no esperado");
            Tools.anadirMensaje(request, "El formulario recibido no se esperaba", 'e');
        }
        request.getRequestDispatcher("/staf/bill_management.jsp").forward(request, response);
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

    private boolean sendMail(HttpServletRequest request, Factura factura, Sucursal suc) {
        String contenido = Tools.leerArchivoClassPath("/plantillaFactura.html");
        if (contenido == null) {
            return false;
        }
        contenido.replace("&NAME_CLI&", factura.getCliente().getName());
        contenido.replace("&DNI_CLI&", factura.getCliente().getDni());
        contenido.replace("&DIR_CLI&", factura.getCliente().getAddress());
        contenido.replace("&TEL_CLI&", factura.getCliente().getTelephone());
        contenido.replace("&MAIL_CLI&", factura.getCliente().getEmail());
        if (factura.getCliente().getCompany() == null){
            contenido.replace("&COMPANY_CLI&", "Cliente Particular");
        }else{
            contenido.replace("&COMPANY_CLI&", factura.getCliente().getCompany());
        }
        contenido.replace("&AGE_CLI&", Integer.toString(factura.getCliente().getAge()));
        
        contenido.replace("&NAME_SUC&", suc.getNombre());
        contenido.replace("&DIR_SUC&", suc.getDir());
        contenido.replace("&TEL_SUC&", suc.getTelefono());
        contenido.replace("&FAX_SUC&", suc.getFax());
        
        contenido.replace("&COD_FAC&", factura.getCodFactura());
        contenido.replace("&IMPORTENOIVA_FAC&", Tools.printBigDecimal(factura.getImporteSinIVA()) + " €");
        contenido.replace("&IVA_FAC&", Integer.toString(factura.getIVA()) + " %");
        contenido.replace("&IMPORTE_FAC&", Tools.printBigDecimal(factura.getImporte()) + " €");
        contenido.replace("&FECHAEMISION_FAC&", Tools.printDate(factura.getFechaEmision()));
        if (factura.isPagado()){
            contenido.replace("&ESTADO_FAC&", "Pagada");
        }else{
            contenido.replace("&ESTADO_FAC&", "Pendiente de pago");
        }
        
        HashMap<String, String> adjuntos = new HashMap<String, String>();
        adjuntos.put(request.getServletContext().getRealPath("/staf/billFolder/" + factura.getCodFactura() + ".pdf"), "Factura_" + factura.getCodFactura() + ".pdf");
        return Tools.emailSend(request, "El Volante Feliz: Factura", factura.getCliente().getEmail(), contenido, adjuntos);
    }
}
