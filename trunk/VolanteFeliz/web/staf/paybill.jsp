<%-- 
    Document   : paybill
    Created on : 14-may-2012, 12:53:26
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Factura"%>
<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% 
if (!validateEntry(request)){
    response.sendError(404);
}
Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Pago Factura</title>
    </head>
    <body>
        <!-- Contenido completo menos footer -->
        <div id="content">
            <!-- Cabecera: Título, subtítulo e imágenes --> 
            <%@include file="/WEB-INF/include/header.jsp" %>

            <!-- Menu horizontal. Activo class=here -->
            <%@include file="/WEB-INF/include/menu.jsp" %>

            <!-- Contenido página -->
            <div id="page">

                <!-- Columna izquierda -->
                <%@include file="/WEB-INF/include/menuLateral.jsp" %>

                <!-- Columna principal -->
                <div class="width75 floatRight">


                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles Factura</h1>
                        <% Factura bill = persistence.getFactura(request.getParameter("bill"), null);
                        if (bill != null && (bill.getCliente().getCodSucursal().equals(suc.getCodSucursal()) || suc.isCentral())){ %>
                        
                        <% } %>
                        
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                </div>
                <!-- FIN COLUMNA PRINCIPAL -->

            </div>
            <!-- Fin contenido página -->
        </div>
        <!-- FIN CONTENIDO -->

        <!-- FOOTER -->
        <%@include file="/WEB-INF/include/footer.jsp" %>
    </body>
</html>


<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>

<%!
private boolean validateEntry (HttpServletRequest request){
    if (request.getParameterMap().size() >= 1 && request.getParameter("bill") != null){
        try{
            Tools.validateUUID(request.getParameter("bill"));
        } catch (ValidationException ex){
            return false;
        }
    }
    return true;
}
%>

