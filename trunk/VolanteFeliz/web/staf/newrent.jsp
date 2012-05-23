<%-- 
    Document   : newrent
    Created on : 20-may-2012, 18:28:31
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="java.util.Date"%>
<%@page import="tools.Tools"%>
<%@page import="model.Tarifa"%>
<%@page import="model.Vehiculo"%>
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
                                    <% if (cli.isActivo()) {%>
                                    <option value="<%= cli.getCodCliente()%>"><%= cli.getName()%>: <%= cli.getDni()%></option>
                                    <% }%>
                                    <% }%>
                                </select>
                            </p>
                            <p>
                                <label>Seleccione una fecha de inicio</label>
                                <input type="text" name="fechainicio" id="fechainicio" readonly="readonly" size="12" class=":date_au :required :only_on_submit" />
                            </p>
                            <p>
                                <label>Seleccione una fecha de fin</label>
                                <input type="text" name="fechafin" id="fechafin" readonly="readonly" size="12" class=":date_au :required :only_on_submit" />
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
                    <% } else if (request.getParameter("st").equals("2")) {
                        HashMap<String, Vehiculo> vehiculos = (HashMap<String, Vehiculo>) request.getAttribute("vehiclesRent");
                        Cliente cli = (Cliente) request.getAttribute("clientRent");
                        HashMap<String, Tarifa> tarifas = persistence.getTarifas(null, null);
                        if (vehiculos != null && cli != null && tarifas != null) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Nuevo Alquiler</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Paso 2 - Selección de vehículo y tarifa</h2>
                        <ul>
                            <li><b>Cliente: </b><%= cli.getName()%></li>
                            <li><b>DNI: </b><%= cli.getDni()%></li>
                            <li><b>Fecha Inicio: </b><%= Tools.printDate((Date)request.getAttribute("fechaInicio"))%></li>
                            <li><b>Fecha Fin: </b><%= Tools.printDate((Date)request.getAttribute("fechaFin"))%></li>
                        </ul>
                        <form name="sel_veh" method="POST" action="/staf/newrent_2">
                            <p>
                                <label>Seleccione un vehículo</label>
                                <select name="vehiculo" class=":required :only_on_blur">
                                    <% for (Vehiculo vehicle : vehiculos.values()) {%>
                                    <option value="<%= vehicle.getCodVehiculo()%>"><%= vehicle.getMarca()%>: <%= vehicle.getModelo()%></option>
                                    <% }%>
                                </select>
                            </p>
                            <p>
                                <label>Seleccione una tarifa</label>
                                <select name="tarifa" class=":required :only_on_blur">
                                    <% for (Tarifa tarif : tarifas.values()) {%>
                                    <option title="<%= tarif.getDescripcion()%>: <%= Tools.printBigDecimal(tarif.getPrecioDia())%> €/día" value="<%= tarif.getCodTarifa()%>"><%= tarif.getNombre()%></option>
                                    <% }%>
                                </select>
                            </p>
                            <p>
                                <label>Inserte los kilómetros actuales del vehículo</label>
                                <input type="text" name="KMInicio" size="30" maxlength="10" class=":digits :required :only_on_blur" />
                            </p>
                            <p>
                                <input type="hidden" name="cli" value="<%= cli.getCodCliente()%>" />
                                <input type="hidden" name="fechainicio" value="<%= Tools.printDate_numMonth((Date)request.getAttribute("fechaInicio")) %>" />
                                <input type="hidden" name="fechafin" value="<%= Tools.printDate_numMonth((Date)request.getAttribute("fechaFin")) %>" />
                                <input type="submit" name="saveRent" value="Confirmar Alquiler" />
                            </p>
                        </form>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% } else if (vehiculos != null && cli != null && tarifas != null) {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>No se puede realizar el alquiler porque no hay tarifas en el sistema</p>
                        </blockquote>
                    </div>
                    <% } else {
                            response.sendError(404);
                        }%>

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