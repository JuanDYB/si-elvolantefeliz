<%-- 
    Document   : viewrent
    Created on : 22-may-2012, 10:30:37
    Author     : JuanDYB
--%>

<%@page import="model.Incidencia"%>
<%@page import="java.util.HashMap"%>
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
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
    Alquiler alq = persistence.getAlquiler(request.getParameter("rent"), null);
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

                    <% if (alq != null && (alq.getCliente().getCodSucursal().equals(suc.getCodSucursal()) || suc.isCentral())) {
                            Cliente cli = alq.getCliente();%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles Alquiler</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>

                        <h2>Informaci&oacute;n del Cliente</h2>
                        <% if (!cli.isActivo()) {%>
                        <blockquote class="exclamation">
                            <p><b>ATENCIÓN: </b>Este cliente se ha dado de baja de la empresa</p>
                            <p>Esta viendo únicamente al cliente porque sus datos son necesarios para el historial de la empresa</p>
                        </blockquote>
                        <% }%>
                        <p><img alt="rent" class="floatRight" src="/images/icons/rentIcon.png"/></p>
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
                        <h2>Información del Alquiler</h2>
                        <ul>
                            <li><b>Código del alquiler: </b><%= alq.getCodAlquiler()%></li>
                            <li><h3>Información del Vehículo</h3>
                                <ul>
                                    <li><b>Marca: </b><%= alq.getVehiculo().getMarca()%></li>
                                    <li><b>Modelo: </b><%= alq.getVehiculo().getModelo()%></li>
                                    <li><b>Matrícula: </b><%= alq.getVehiculo().getMatricula()%></li>
                                </ul>
                            </li>
                            <li><b>Fecha de Inicio: </b><%= Tools.printDate(alq.getFechaInicio()) %></li>
                            <li><b>Fecha de Fin: </b><%= Tools.printDate(alq.getFechaFin()) %></li>
                            <li><b>Kilómetros iniciales: </b><%= alq.getKMInicio() %> Kilómetros</li>
                            <li><h3>Estado del alquiler</h3>
                                <ul>
                                    <%  if (alq.getFechaEntrega() == null){ %>
                                    <li>Alquiler no finalizado</li>
                                    <% } else{ %>
                                    <li><b>Fecha de Entrega: </b><%= Tools.printDate(alq.getFechaEntrega()) %></li>
                                    <li><b>Estado del depósito de combustible: </b><%= alq.getCombustibleFin() %> / <%= alq.getVehiculo().getCapacidadCombustible() %> Litros</li>
                                    <li><b>Kilómetros Finales: </b><%= alq.getKMFin() %> Kilómetros</li>
                                    <li><b>Precio Final (Sin IVA): </b><%= Tools.printBigDecimal(alq.getPrecio()) %> €</li>
                                    <% } %>
                                </ul>
                            </li>
                            <li><h3>Información de la Tarifa</h3>
                                <ul>
                                    <li><b>Nombre: </b><%= alq.getTarifa().getNombre() %></li>
                                    <li><b>Descripción: </b><%= alq.getTarifa().getDescripcion() %></li>
                                    <li><b>Precio inicial: </b><%= Tools.printBigDecimal(alq.getTarifa().getPrecioBase()) %> €</li>
                                    <li><b>Precio por día: </b><%= Tools.printBigDecimal(alq.getTarifa().getPrecioDia()) %> €</li>
                                    <li><b>Precio por día extra: </b><%= Tools.printBigDecimal(alq.getTarifa().getPrecioDiaExtra()) %> €</li>
                                    <li><b>Precio por litro de combustible: </b><%= Tools.printBigDecimal(alq.getTarifa().getPrecioCombustible()) %> €</li>
                                </ul>
                            </li>
                        </ul>
                                <% if(alq.getObservaciones() != null){ %>
                                <h3>Observaciones</h3>
                                <p><%= alq.getObservaciones() %></p>
                                <% } %>
                    </div>
                    <div class="gradient">
                        <h1>Incidencias asociadas</h1>
                                <% HashMap <String, Incidencia> incidenciasAlquiler = persistence.getIncidencias("codAlquiler", alq.getCodAlquiler(), null);
                                if (incidenciasAlquiler != null){ %>
                                <table>
                                    <tr class="theader"><td>Tipo</td><td>Descripción</td><td>Fecha</td><td>Importe</td><td>&nbsp;</td></tr>
                                    <% for (Incidencia inc: incidenciasAlquiler.values()) { %>
                                    <tr>
                                        <td><%= inc.getTipoIncidencia().getNombre() %></td>
                                        <td><%= inc.getObservaciones() %></td>
                                        <td><%= Tools.printDate(inc.getFecha()) %></td>
                                        <td><%= Tools.printBigDecimal(inc.getPrecio()) %> €</td>
                                        <td><a title="Detalles Incidencia"  href="/staf/viewincidence.jsp?inc=<%= inc.getCodIncidencia() %>"><img src="/images/icons/view_incidence.png" alt="ver incidencia" /></a></td>
                                    </tr>
                                    <% } %>
                                </table>
                                <% }else{ %>
                                <blockquote class="go">
                                    <p>El alquiler actual no tiene incidencias asociadas</p>
                                </blockquote>
                                <% } %>
                    </div>
                    <% } else if (alq != null && !alq.getCliente().getCodSucursal().equals(suc.getCodSucursal()) && !suc.isCentral()) {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No tiene permisos de ver los derechos de este cliente porque no pertenece a esta sucursal y esta no es la sucursal central
                            </p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No se ha encontrado el alquiler seleccionado
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
                Tools.validateUUID(request.getParameter("rent"));
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
