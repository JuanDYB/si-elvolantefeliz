<%-- 
    Document   : viewbill
    Created on : 14-may-2012, 11:01:37
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Factura"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
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
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Detalles Factura</title>
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
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Detalles generales</h2>
                        <ul>
                            <li><b>Codigo Factura: </b><%= bill.getCodFactura() %></li>
                            <li><b>Cliente: </b><a title="Ver Detalles del cliente" href="/staf/viewclient.jsp?cli=<%= bill.getCliente().getCodCliente() %>"><%= bill.getCliente().getName() %></a></li>
                            <% if (bill.getCliente().getCompany() == null){ %>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% }else{ %>
                            <li><b>Empresa: </b><%= bill.getCliente().getCompany() %></li>
                            <% } %>
                        </ul>
                        <h2>Detalles Económicos</h2>
                        <ul>
                            <li><b>Importe: </b><%= Tools.printBigDecimal(bill.getImporteSinIVA()) %> €</li>
                            <li><b>IVA: </b><%= bill.getIVA() %> %</li>
                            <li><b>Importe con IVA: </b><%= Tools.printBigDecimal(bill.getImporte()) %> €</li>
                            <li><b>Fecha Emisión: </b><%= Tools.printDate (bill.getFechaEmision()) %></li>
                            <% if (!bill.isPagado()){ %>
                            <li><b>Pagado: </b>Factura pendiente de pago</li>
                            <img src="/images/icons/pay.png" alt="Pagar Factura"><a title="Haga click para pagar la factura" href="/staf/paybill.jsp?bill=<%= bill.getCodFactura() %>">Pagar Factura</a>
                            <% }else{ %>
                            <li><b>Pagado: </b>Factura pagada</li>
                            <li><b>Fecha de pago: </b><%= Tools.printDate(bill.getFechaPago()) %></li>
                            <li><b>Forma de pago: </b><%= bill.getFormaPago() %></li>
                            <% } %>
                        </ul>
                        <h2>Detalles de la factura completa</h2>
                        <p>
                            <img class="floatLeft" src="/images/icons/pdf.png" alt="pdf"/>
                            <a title="Ver factura completa" href="/staf/billFolder/<%= bill.getCodFactura() %>.pdf">Ver Factura completa en PDF</a>
                        </p>
                        <% }else if(bill != null && !bill.getCliente().getCodSucursal().equals(suc.getCodSucursal()) && !suc.isCentral()){ %>
                        <blockquote class="exclamation">
                            <p>
                                No tiene permisos para mostrar esta factura porque no pertenece a su sucursal y tampoco es la sucursal central
                            </p>
                        </blockquote>
                        <% }else{ %>
                        <blockquote class="stop">
                            <p>
                                Ocurrio un error obteniendo la factura. Es posible que la factura solicitada no exista
                            </p>
                        </blockquote>
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
