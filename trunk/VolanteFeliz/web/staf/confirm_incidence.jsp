<%-- 
    Document   : confirm_incidence
    Created on : 26-may-2012, 18:23:40
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Incidencia"%>
<%@page import="tools.Tools"%>
<%@page import="model.TipoIncidencia"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Date"%>
<%@page import="model.Alquiler"%>
<%@page import="java.util.HashMap"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
if (!this.validateForm(request)){
    response.sendError(404);
}
PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
HashMap <String, Alquiler> alquileresPosibles = (HashMap <String, Alquiler>) request.getAttribute("posiblesAlq");
String matricula = (String) request.getAttribute("matricula");
Date fecha = (Date) request.getAttribute("fecha");
BigDecimal precio = (BigDecimal) request.getAttribute("precio");
String observaciones = (String) request.getAttribute("observaciones");
TipoIncidencia tipoIncidencia = (TipoIncidencia) request.getAttribute("tipoInc");
Incidencia inc = new Incidencia(null, tipoIncidencia, null, null, fecha, observaciones, precio);
session.setAttribute("incidenciaEnCurso", inc);
session.setAttribute("posiblesAlquileres", alquileresPosibles);
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Confirmación Incidencia</title>
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
                    <div class="gradient">
                        <h1>Confirmar Nueva Incidencia</h1>
                        <h2>Detalles de la Incidencia</h2>
                        <ul>
                            <li><b>Tipo Incidencia: </b><%= tipoIncidencia.getNombre() %></li>
                            <li><b>Matrícula: </b><%= matricula %></li>
                            <li><b>Fecha: </b><%= Tools.printDate(fecha) %></li>
                            <li><b>Precio: </b><%= Tools.printBigDecimal(precio) %> €</li>
                        </ul>
                        <h3>Observaciones</h3>
                        <p><%= observaciones %></p>
                        <h2>Selecciona el alquiler al que asignar la incidencia</h2>
                        <form method="POST" action="/staf/confirmincidence" name="confirmInc">
                            <table>
                                <tr class="theader"><td>&nbsp;</td><td>Marca</td><td width="130" >Modelo</td><td width="170" >Cliente</td><td>F. Inicio</td><td>F. Fin</td><td>&nbsp;</td></tr>
                            <% for (Alquiler alq: alquileresPosibles.values()){ %>
                            <tr>
                                <td><input type="Radio" name="alquiler" value="<%= alq.getCodAlquiler()%>" /></td>
                                <td><%= alq.getVehiculo().getMarca() %></td>
                                <td><%= alq.getVehiculo().getModelo() %></td>
                                <td><%= alq.getCliente().getName() %></td>
                                <td><%= Tools.printDate(alq.getFechaInicio()) %></td>
                                <% if (alq.getFechaEntrega() == null) { %>
                                <td><%= Tools.printDate(alq.getFechaFin()) %></td>
                                <% }else{ %>
                                <td>Entregado<br /><%= Tools.printDate(alq.getFechaInicio()) %></td>
                                <% } %>
                                <td><a title="Detalles Alquiler" href="/staf/viewrent.jsp?rent=<%= alq.getCodAlquiler() %>"><img alq="verAlq" src="/images/icons/viewRent.png"/></a></td>
                            </tr>
                            <% } %>
                        </table>
                        <p>
                            <input type="hidden" name="matricula" value="<%= matricula %>" />
                            <input type="submit" name="confirmInc" value="Confirmar Datos" />
                        </p>
                        </form>
                    </div>
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

<%!
private boolean validateForm (HttpServletRequest request){
    if (request.getAttribute("tipoInc") != null && request.getAttribute("matricula") != null && request.getAttribute("fecha") != null 
            && request.getAttribute("precio") != null && request.getAttribute("observaciones") != null 
            && request.getAttribute("posiblesAlq") != null){
        return true;
    }
    return false;
}
%>

<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
