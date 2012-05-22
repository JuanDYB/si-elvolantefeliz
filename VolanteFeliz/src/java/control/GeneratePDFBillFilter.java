package control;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import tools.PDFAutoGeneration;

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
        StringBuffer peticion = httpRequest.getRequestURL();
        int fin = peticion.lastIndexOf(".pdf");
        String codFactura = peticion.substring(fin - 36, fin).toString();
        PDFAutoGeneration generator = new PDFAutoGeneration(httpRequest, codFactura);
        if (generator.validateAccessAndGenerate()){
            chain.doFilter(request, response);
        }else{
            request.getRequestDispatcher("/WEB-INF/errorPage/pdferror.jsp").forward(request, response);
        }
    }

    

    @Override
    public void destroy() {
    }
}
