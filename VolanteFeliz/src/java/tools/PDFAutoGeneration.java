package tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import model.Empleado;
import model.Factura;
import model.Sucursal;
import persistence.PersistenceInterface;

/**
 *
 * @author Juan Díez-Yanguas
 */
public class PDFAutoGeneration {
    private HttpServletRequest request;
    private String codFactura;

    public PDFAutoGeneration(HttpServletRequest request, String codFactura) {
        this.request = request;
        this.codFactura = codFactura;
    }
    
    public boolean validateAccessAndGenerate (){
        Empleado empl = (Empleado) request.getSession().getAttribute("empleado");
        PersistenceInterface persistence = (PersistenceInterface) request.getServletContext().getAttribute("persistence");
        Sucursal sucActiva = persistence.getSucursal(empl.getCodSucursal());
        
        Factura factura = persistence.getFactura(codFactura, null);
        Sucursal sucFactura = this.getSucursalFactura(persistence, sucActiva, factura.getCliente().getCodSucursal());

        if (factura != null) {
            if (factura.getCliente().getCodSucursal().equals(empl.getCodSucursal()) || sucActiva.isCentral()) {
                if (Tools.existeArchivo(request.getServletContext().getRealPath("/staf/billFolder/" + codFactura + ".pdf"))) {
                    return true;
                } else {
                    if (sucFactura == null) {
                        request.setAttribute("resultados", "Sucursal no encontrada");
                        Tools.anadirMensaje(request, "La sucursal a la que pertenece la factura no ha sido encontrada", 'e');
                        return false;
                    }
                    GeneratePDFBill pdfBill = new GeneratePDFBill(factura, sucFactura, request.getServletContext().getRealPath("/"));
                    if (pdfBill.generateBill()) {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Se regenero el PDF de la factura");
                        return true;
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error regenerando el pfd de la factura");
                        request.setAttribute("resultados", "Error generando factura");
                        Tools.anadirMensaje(request, "Ha ocurrido un error generando el documento de la factura", 'e');
                        return false;
                    }
                }
            } else {
                request.setAttribute("resultados", "Permisos no válidos");
                Tools.anadirMensaje(request, "No puede ver la factura que intenta ver porque no pertenece a esta sucursal y tampoco es una sucursal central", 'w');
            }
        } else {
            request.setAttribute("resultados", "Factura no encontrada");
            Tools.anadirMensaje(request, "No puede ver la factura que intenta visualizar porque no existe", 'e');
        }
        return false;
    }
    
    private Sucursal getSucursalFactura(PersistenceInterface persistence, Sucursal sucActiva, String codSucursalFactura) {
        if (codSucursalFactura.equals(sucActiva.getCodSucursal())) {
            return sucActiva;
        } else {
            return persistence.getSucursal(codSucursalFactura);
        }
    }
}
