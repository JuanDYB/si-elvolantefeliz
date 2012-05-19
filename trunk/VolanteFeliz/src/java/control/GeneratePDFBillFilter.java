package control;

import java.io.IOException;
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
        Sucursal sucFactura = this.getSucursalFactura(persistence, sucActiva, codFactura);
        if (sucFactura == null) {
            httpRequest.setAttribute("resultados", "Sucursal no encontrada");
            Tools.anadirMensaje(httpRequest, "La sucursal a la que pertenece la factura no ha sido encontrada", 'e');
            httpRequest.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
        }

        if (factura != null) {
            if (factura.getCliente().getCodSucursal().equals(empl.getCodSucursal()) || sucActiva.isCentral()) {
                if (Tools.existeArchivo(httpRequest.getServletContext().getRealPath("/staf/billFolder/" + codFactura + ".pdf"))) {
                    chain.doFilter(request, response);
                } else {
                    GeneratePDFBill pdfBill = new GeneratePDFBill(factura, sucFactura, httpRequest.getServletContext().getRealPath("/"));
                    if (pdfBill.generateBill()) {
                        chain.doFilter(request, response);
                    } else {
                        httpRequest.setAttribute("resultados", "Error generando factura");
                        Tools.anadirMensaje(httpRequest, "Ha ocurrido un error generando el documento de la factura", 'e');
                        httpRequest.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
                        return;
                    }
                }
                chain.doFilter(request, response);
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
        Sucursal sucFactura = null;
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
