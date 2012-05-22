<%-- 
    Document   : viewrent
    Created on : 22-may-2012, 10:30:37
    Author     : JuanDYB
--%>

<%@page import="model.Alquiler"%>
<%@page import="model.Empleado"%>
<%@page import="model.Cliente"%>
<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Cliente cli = persistence.getClient(request.getParameter("cli"));
    Sucursal suc = persistence.getSucursal(cli.getCodSucursal());
    Alquiler alq = persistence.getAlquiler(request.getParameter("rent"));
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Detalles alquiler</title>
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

                    <% if (alq != null) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles Alquiler</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>

                        <h2>Informaci&oacute;n General del Cliente</h2>
                        <ul>

                            <li><b>Nombre: </b><%= cli.getName()%></li>
                            <li><b>Email: </b><%= cli.getEmail()%></li>
                            <li><b>DNI: </b><%= cli.getDni()%></li>
                            <li><b>Dirección: </b><%= cli.getAddress()%></li>
                            <li><b>Tel&eacute;fono: </b><%= cli.getTelephone()%></li>
                            <% if (cli.getCompany() == null) {%>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% } else {%>
                            <li><b>Empresa: </b><%= cli.getCompany()%></li>
                            <% }%>
                            <li><b>Edad: </b><%= cli.getAge()%></li>

                        </ul>
                    </div>
                            <div class="gradient">
                                <h1>Acciones disponibles</h1>
                                <ul>
                                    <li><a href="/staf/client-facturepending.jsp?type=all&cli=<%= cli.getCodCliente()%>">Elementos pendientes de facturar</a></li>
                                    <li><a href="/staf/pending_paybill.jsp?cli=<%= cli.getCodCliente()%>">Facturas pendientes de pago</a></li>
                                    <li><a href="/staf/client_history.jsp?cli=<%= cli.getCodCliente() %>" >Historial completo del cliente</a></li>
                                </ul>
                            </div>
                    <% } else if (cli != null && !suc.getCodSucursal().equals(cli.getCodSucursal()) && !suc.isCentral()) {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No tiene permisos de ver los derechos de este cliente porque no pertenece a esta sucursal
                            </p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No se ha encontrado el cliente seleccionado
                            </p>
                        </blockquote>
                    </div>
                    <% }%>
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

<%! private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("rent") != null) {
            try {
                Tools.validateUUID(request.getParameter("cli"));
                return true;
            } catch (ValidationException ex) {
                return false;
            }
        }
        return false;
    }
%>

<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
