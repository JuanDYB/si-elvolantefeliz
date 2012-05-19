package control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import model.Empleado;
import model.Factura;
import model.Sucursal;
import persistence.PersistenceInterface;
import tools.GeneratePDFBill;
import tools.Tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class GeneratePDFBillFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Empleado empl = (Empleado) httpRequest.getSession().getAttribute("empleado");
        PersistenceInterface persistence = (PersistenceInterface) httpRequest.getServletContext().getAttribute("persistence");
        Sucursal sucActiva = persistence.getSucursal(empl.getCodSucursal());
        StringBuffer peticion = httpRequest.getRequestURL();
        int fin = peticion.lastIndexOf(".pdf");
        String codFactura = peticion.substring(fin - 36, fin).toString();
        Factura factura = persistence.getFactura(codFactura, null);
        Sucursal sucFactura = this.getSucursalFactura(persistence, sucActiva, factura.getCliente().getCodSucursal());

        if (factura != null) {
            if (factura.getCliente().getCodSucursal().equals(empl.getCodSucursal()) || sucActiva.isCentral()) {
                if (Tools.existeArchivo(httpRequest.getServletContext().getRealPath("/staf/billFolder/" + codFactura + ".pdf"))) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    if (sucFactura == null) {
                        httpRequest.setAttribute("resultados", "Sucursal no encontrada");
                        Tools.anadirMensaje(httpRequest, "La sucursal a la que pertenece la factura no ha sido encontrada", 'e');
                        httpRequest.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
                        return;
                    }
                    GeneratePDFBill pdfBill = new GeneratePDFBill(factura, sucFactura, httpRequest.getServletContext().getRealPath("/"));
                    if (pdfBill.generateBill()) {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Se regenero el PDF de la factura");
                        chain.doFilter(request, response);
                        return;
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error regenerando el pfd de la factura");
                        httpRequest.setAttribute("resultados", "Error generando factura");
                        Tools.anadirMensaje(httpRequest, "Ha ocurrido un error generando el documento de la factura", 'e');
                        httpRequest.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
                        return;
                    }
                }
            } else {
                httpRequest.setAttribute("resultados", "Permisos no válidos");
                Tools.anadirMensaje(httpRequest, "No puede ver la factura que intenta ver porque no pertenece a esta sucursal y tampoco es una sucursal central", 'w');
            }
        } else {
            httpRequest.setAttribute("resultados", "Factura no encontrada");
            Tools.anadirMensaje(httpRequest, "No puede ver la factura que intenta visualizar porque no existe", 'e');
        }
        httpRequest.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
    }

    private Sucursal getSucursalFactura(PersistenceInterface persistence, Sucursal sucActiva, String codSucursalFactura) {
        if (codSucursalFactura.equals(sucActiva.getCodSucursal())) {
            return sucActiva;
        } else {
            return persistence.getSucursal(codSucursalFactura);
        }
    }

    @Override
    public void destroy() {
    }
}
