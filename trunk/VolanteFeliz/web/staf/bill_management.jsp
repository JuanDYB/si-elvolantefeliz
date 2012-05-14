<%-- 
    Document   : bill_management
    Created on : 15-abr-2012, 12:36:10
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="tools.Tools"%>
<%@page import="com.sun.xml.ws.rx.rm.runtime.ClientAckRequesterTask"%>
<%@page import="model.Factura"%>
<%@page import="java.util.HashMap"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% 
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
        <title>Zona de empleados</title>
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
                        <h1>Gesti&oacute;n Facturaci&oacute;n</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Acciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/clients-pendingfacture.jsp">Nueva Factura</a></li>
                            <li><a href="/staf/pending_paybill.jsp">Pagar Factura</a></li>
                            <% if (suc.isCentral()){ %>
                            <li>Sucursal Central: <a href="/staf/bill_management.jsp?all=1">Ver todas las facturas</a></li>
                            <% } %>
                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Facturas disponibles</h1>
                        <a
                        <%
                        HashMap <String, Factura> facturas;
                        if (suc.isCentral() && request.getParameter("all") != null && request.getParameter("all").equals("1")){
                            facturas = persistence.getFacturas ("1", "1", null);
                        } else{
                            facturas = persistence.getFacturas ("1", "1", emplLogedIn.getCodSucursal());
                        }
                        if (facturas != null){
                        %>
                        <table>
                            <tr class="theader"><td>Cliente</td><td>Fecha</td><td>Importe</td></tr>
                            <% for (Factura fact: facturas.values()){ %>
                            <tr>
                                <td><%= fact.getCliente().getName() %></td>
                                <td><%= fact.getFechaEmision() %></td>
                                <td><%= Tools.printBigDecimal(fact.getImporte()) %></td>
                                <td><a title="Detalles Factura" href="/staf/viewbill.jsp?bill=<%= fact.getCodFactura() %>"</td>
                                <% if (fact.isPagado()){ %>
                            <img src="/images/icons/facPagada.png" alt="facPagada" title="Factura Pagada" />
                                <% } else{ %>
                            <a title="Pagar Factura" href="/staf/paybill.jsp?bill=<%= fact.getCodFactura() %>"><img src="/images/icons/pay.png" alt="pagarFactura" /></a>
                                <% }%>
                            </tr>
                            <% } %>
                        </table>
                        <% } else{ %>
                        <h2>No se han encontrado facturas en el sistema</h2>
                        <% } %>
                        <ul>
                            <li><a href="/staf/clients-pendingfacture.jsp">Nueva Factura</a></li>
                            <li><a href="/staf/pending_paybill.jsp">Pagar Factura</a></li>
                        </ul>
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
