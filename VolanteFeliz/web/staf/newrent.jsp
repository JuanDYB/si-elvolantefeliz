<%-- 
    Document   : newrent
    Created on : 20-may-2012, 18:28:31
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Cliente"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!this.validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.7.2.custom.css" />
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
        
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        <%@include file="/WEB-INF/include/calendar.jsp" %>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Nuevo Cliente</title>
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

                    <% if (request.getParameter("st").equals("1")) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Nuevo Alquiler</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Paso 1 - Datos iniciales</h2>
                        <% HashMap<String, Cliente> clientes = persistence.getClients("codSucursal", emplLogedIn.getCodSucursal());
                            if (clientes != null) {%>

                        <form name="sel_cli" method="POST" action="/staf/newrent_1">
                            <p>
                                <label>Seleccione un cliente</label>
                                <select name ="cliente" class=":required : only_on_blur">
                                    <% for (Cliente cli : clientes.values()) {%>
                                    <option value="<%= cli.getCodCliente()%>"><%= cli.getName()%>: <%= cli.getDni()%></option>
                                    <% }%>
                                </select>
                            </p>
                            <p>
                                <label>Seleccione una fecha de inicio</label>
                                <input type="text" name="fechainicio" id="fechainicio" readonly="readonly" size="12" />
                            </p>
                            <p>
                                <label>Seleccione una fecha de fin</label>
                                <input type="text" name="fechafin" id="fechafin" readonly="readonly" size="12" />
                            </p>
                            <p>
                                <input type="submit" name="continue" value="Continuar>>" />
                            </p>
                        </form>
                        <% } else {%>
                        <blockquote class="exclamation">
                            <p>No se han encontrado clientes en la sucursal, no puede realizar un alquiler</p>
                        </blockquote>
                        <% }%>

                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% } else if (request.getParameter("st").equals("2")) {%>

                    <% }%>
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
    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("st") != null) {
            try {
                Integer.parseInt(request.getParameter("st"));
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return false;
    }
%>
