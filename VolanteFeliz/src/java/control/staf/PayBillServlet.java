package control.staf;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class PayBillServlet extends HttpServlet {

    HashMap<String, String> formasPago = new HashMap<String, String>();

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
            String codFactura = request.getParameter("bill");
            String formaPago = formasPago.get(request.getParameter("formaPago"));
            PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
            Factura facturaSinPagar = persistence.getFactura(codFactura, null);
            if (facturaSinPagar != null) {
                Empleado emplSesion = (Empleado) request.getSession().getAttribute("empleado");
                Sucursal suc = persistence.getSucursal(emplSesion.getCodSucursal());
                if (emplSesion.getCodSucursal().equals(facturaSinPagar.getCliente().getCodSucursal()) || suc.isCentral()) {
                    Date fechaActual = Tools.getDate();
                    if (persistence.pagarFactura(codFactura, fechaActual, formaPago)) {
                        request.setAttribute("resultados", "Factura actualizada");
                        Tools.anadirMensaje(request, "La factura ha sido actualizada correctamente con el pago realizado", 'o');
                        if (this.sendMail(request, facturaSinPagar, suc)){
                            Tools.anadirMensaje(request, "Se ha enviado correctamente la confirmación de pago al cliente", 'o');
                        }else{
                            Tools.anadirMensaje(request, "Ha ocurrido un error al enviar al cliente la confirmación de pago", 'w');
                        }
                        request.getRequestDispatcher("/staf/viewbill.jsp?bill=" + codFactura).forward(request, response);
                    } else {
                        request.setAttribute("resultados", "Error actualizando factura");
                        Tools.anadirMensaje(request, "Ha ocurrido un error actualizando la factura", 'e');
                        request.getRequestDispatcher("/staf/paybill.jsp?bill=" + codFactura).forward(request, response);
                    }
                    return;
                } else {
                    request.setAttribute("resultados", "Permisos insuficientes");
                    Tools.anadirMensaje(request, "No se pudo actualizar la factura, no pertenece a la sucursal de la factura y no esta en una sucursal central", 'w');
                }
            } else {
                request.setAttribute("resultados", "Factura no encontrada");
                Tools.anadirMensaje(request, "No se ha encontrado la factura que desea pagar", 'e');
            }

        } else {
            request.setAttribute("resultados", "Formulario no válido");
            Tools.anadirMensaje(request, "El formato de formulario enviado no es correcto", 'w');
        }
        request.getRequestDispatcher("/staf/bill_management.jsp").forward(request, response);
    }

    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 2 && request.getParameter("formaPago") != null && request.getParameter("bill") != null
                && request.getParameter("pay") != null) {
            try {
                if (formasPago.containsKey(request.getParameter("formaPago"))) {
                    Tools.validateUUID(request.getParameter("bill"));
                    return true;
                }
            } catch (ValidationException ex) {
                return false;
            }
        }
        return false;
    }
    
    private boolean sendMail(HttpServletRequest request, Factura factura, Sucursal suc) {
        String contenido = Tools.leerArchivoClassPath("/plantillaPago.html");
        if (contenido == null) {
            return false;
        }
        contenido = contenido.replace("&NAME_CLI&", factura.getCliente().getName());
        contenido = contenido.replace("&DNI_CLI&", factura.getCliente().getDni());
        contenido = contenido.replace("&DIR_CLI&", factura.getCliente().getAddress());
        contenido = contenido.replace("&TEL_CLI&", factura.getCliente().getTelephone());
        contenido = contenido.replace("&MAIL_CLI&", factura.getCliente().getEmail());
        if (factura.getCliente().getCompany() == null){
            contenido = contenido.replace("&COMPANY_CLI&", "Cliente Particular");
        }else{
            contenido = contenido.replace("&COMPANY_CLI&", factura.getCliente().getCompany());
        }
        contenido = contenido.replace("&AGE_CLI&", Integer.toString(factura.getCliente().getAge()));
        
        contenido = contenido.replace("&NAME_SUC&", suc.getNombre());
        contenido = contenido.replace("&DIR_SUC&", suc.getDir());
        contenido = contenido.replace("&TEL_SUC&", suc.getTelefono());
        contenido = contenido.replace("&FAX_SUC&", suc.getFax());
        
        contenido = contenido.replace("&COD_FAC&", factura.getCodFactura());
        contenido = contenido.replace("&IMPORTENOIVA_FAC&", Tools.printBigDecimal(factura.getImporteSinIVA()) + " €");
        contenido = contenido.replace("&IVA_FAC&", Integer.toString(factura.getIVA()) + " %");
        contenido = contenido.replace("&IMPORTE_FAC&", Tools.printBigDecimal(factura.getImporte()) + " €");
        contenido = contenido.replace("&FECHAEMISION_FAC&", Tools.printDate(factura.getFechaEmision()));
        
        contenido = contenido.replace("&PAY_DATE&", Tools.printDate(factura.getFechaPago()));
        contenido = contenido.replace("&PAY_WAY&", factura.getFormaPago());
        
        HashMap<String, String> adjuntos = new HashMap<String, String>();
        adjuntos.put(request.getServletContext().getRealPath("/staf/billFolder/" + factura.getCodFactura() + ".pdf"), "Factura_" + factura.getCodFactura() + ".pdf");
        return Tools.emailSend(request, "El Volante Feliz: Factura", factura.getCliente().getEmail(), contenido, adjuntos);
    }

    @Override
    public void init(ServletConfig config) {
        formasPago.put("cash", "Efectivo");
        formasPago.put("card", "Tarjeta de crédito");
        formasPago.put("check", "Talon bancario");
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
