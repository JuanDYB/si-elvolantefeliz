<%-- 
    Document   : clients_rentingfacture
    Created on : 09-abr-2012, 10:22:33
    Author     : Juan Jose Olivares
--%>

<%@page import="model.Empleado"%>
<%@page import="model.Cliente"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Incidencia"%>
<%@page import="model.Alquiler"%>

<% if (validateEntry(request) == false) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Cliente cliente = persistence.getClient(request.getParameter("cli"));
    String type = request.getParameter("type");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Facturacion cliente</title>
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
                        <h1>Facturación Cliente</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <% if (cliente != null && (cliente.getCodSucursal().equals(emplLogedIn.getCodSucursal()) || suc.isCentral())) {
                                boolean empresa = false;
                                if (cliente.getCompany() != null) {
                                    empresa = true;
                                }
                        %>
                        <h2>Informaci&oacute;n General del Cliente</h2>
                        <ul>

                            <li><b>Nombre: </b><%= cliente.getName()%></li>
                            <li><b>Email: </b><%= cliente.getEmail()%></li>
                            <li><b>DNI: </b><%= cliente.getDni()%></li>
                            <li><b>Dirección: </b><%= cliente.getAddress()%></li>
                            <li><b>Tel&eacute;fono: </b><%= cliente.getTelephone()%></li>
                            <% if (!empresa) {%>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% } else {%>
                            <li><b>Empresa: </b><%= cliente.getCompany()%></li>
                            <% }%>
                            <li><b>Edad: </b><%= cliente.getAge()%></li>

                        </ul>
                        <%
                            HashMap<String, Incidencia> incSinFacturarCliente = persistence.getIncidenciasClienteSinFacturar(cliente);
                            HashMap<String, Alquiler> alqSinFacturarCliente = persistence.getAlquileresClienteSinFacturar(cliente);
                            if (alqSinFacturarCliente != null || incSinFacturarCliente != null) {
                        %>
                        <h2>Personalización de factura</h2>
                        <form name="elegirAlquileresFactura" action="/staf/genbill" method="POST" >
                            <% }%>
                            <% if (type.equals("all") || type.equals("alq")) {%>
                            <h2>Alquileres pendientes de facturar</h2>
                            <% if (alqSinFacturarCliente != null) {%>
                            <table>
                                <tr class="theader"><td>&nbsp;</td><td>Fecha Salida</td><td>Fecha Entrada</td><td>Matricula</td><td>Marca</td><td>Importe</td></tr>
                                <% for (Alquiler alq : alqSinFacturarCliente.values()) {%>
                                <tr>
                                    <% if (empresa) {%>
                                    <td><input type="checkbox" name="alquiler" value="<%= alq.getCodAlquiler()%>" /></td>
                                        <% } else {%>
                                    <td><input type="Radio" name="alquiler" value="<%= alq.getCodAlquiler()%>" /></td>
                                        <% }%>
                                    <td><%= Tools.printDate(alq.getFechaInicio())%></td>
                                    <td><%= Tools.printDate(alq.getFechaEntrega())%></td>
                                    <td><%= alq.getVehiculo().getMatricula()%></td>
                                    <td><%= alq.getVehiculo().getMarca()%></td>
                                    <td><%= Tools.printBigDecimal(alq.getPrecio())%> €</td>
                                </tr>
                                <% }%>
                            </table>
                            <% } else {%>
                            <blockquote class="go" >
                                <p>No se han encontrado alquileres pendientes de facturar</p>
                            </blockquote>
                            <% }
                                }
                                if (type.equals("all") || type.equals("inc")) {%>
                            <h2>Incidencias pendientes de facturar</h2>
                            <% if (incSinFacturarCliente != null) {%>

                            <table>
                                <tr class="theader"><td>&nbsp;</td><td>Tipo Incidencia</td><td>Fecha</td><td>Importe</td></tr>
                                <% for (Incidencia inc : incSinFacturarCliente.values()) {%>
                                <tr>
                                    <td><input type="checkbox" name="incidencia" value="<%= inc.getCodIncidencia()%>" /></td>
                                    <td><%= inc.getTipoIncidencia().getNombre()%></td>
                                    <td><%= Tools.printDate(inc.getFecha())%></td>
                                    <td><%= Tools.printBigDecimal(inc.getPrecio())%> €</td>
                                </tr>
                                <% }%>
                            </table>
                            <% } else {%>
                            <blockquote class="go" >
                                <p>No se han encontrado incidencias pendientes de facturar</p>
                            </blockquote>
                            <% }%>


                            <% }%>
                            <% if (alqSinFacturarCliente != null || incSinFacturarCliente != null) {%>
                            <input name="cliente" type="hidden" value="<%= cliente.getCodCliente()%>" />
                            <input name="genFact" type="submit" value="Generar Factura" />
                        </form>
                        <% }
                        } else if (cliente != null && !cliente.getCodSucursal().equals(emplLogedIn.getCodSucursal())) {%>
                        <blockquote class="exclamation">
                            <p>No puede generar un factura de este cliente, no pertenece a esta sucursal</p>
                        </blockquote>
                        <% } else {%>
                        <blockquote class="exclamation" />
                            <p>El cliente seleccionada para la generación de factura no ha sido encontrado</p>
                        </blockquote>
                        <% }%>
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

<%! private boolean validateEntry(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 2 && request.getParameter("cli") != null && request.getParameter("type") != null) {
            if (request.getParameter("type").equals("all") || request.getParameter("type").equals("alq")
                    || request.getParameter("type").equals("inc")) {
                try {
                    Tools.validateUUID(request.getParameter("cli"));
                    return true;
                } catch (ValidationException ex) {
                    return false;
                }
            }
        }
        return false;
    }%>


<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>

