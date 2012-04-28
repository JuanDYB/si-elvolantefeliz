<%-- 
    Document   : viewclient
    Created on : 16-abr-2012, 19:02:32
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="tools.Tools"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="model.Empleado"%>
<%@page import="model.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Cliente cli = persistence.getClient(request.getParameter("cli"));
    Sucursal suc = persistence.getSucursal(cli.getCodSucursal());

    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Detalles cliente</title>
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
                        <h1>Detalles Cliente</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Informaci&oacute;n General del Cliente</h2>
                        <ul>

                            <li><b>Nombre: </b><%= cli.getName()%></li>
                            <li><b>Email: </b><%= cli.getEmail()%></li>
                            <li><b>DNI: </b><%= cli.getDni()%></li>
                            <li><b>Dirección: </b><%= cli.getAddress()%></li>
                            <li><b>Tel&eacute;fono: </b><%= cli.getTelephone()%></li>
                            <% if (cli.getCompany() == null){ %>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% } else{ %>
                            <li><b>Empresa: </b><%= cli.getCompany() %></li>
                            <% } %>
                            <li><b>Edad: </b><%= cli.getAge()%></li>

                        </ul>
                        <h2>Informaci&oacute;n General de la sucursal</h2>
                        <% if (suc != null) {%>
                        <ul>
                            <li><b>Sucursal: </b><%= suc.getNombre()%></li>
                            <li><b>Nombre: </b><%= suc.getDir()%></li>
                            <li><b>Teléfono: </b><%= suc.getTelefono()%></li>
                            <li><b>Fax: </b><%= suc.getFax()%></li>
                            <li><b>Direcci&oacute;n: </b><%= suc.getDir()%></li>
                        </ul>
                        <% } else {%>
                        <blockquote class="stop">
                            <p>
                                Ha ocurrido un error obteniendo la sucursal al que pertenece el empleado
                            </p>
                        </blockquote>
                        <% }%> 
                    </div>
                    <div class="gradient">
                        <a href="/staf/client-facturepending.jsp?type=alq&cli=<%= cli.getCodCliente()%>">Ver Facturas pendientes de pagar</a>
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

<%! private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("cli") != null) {
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
