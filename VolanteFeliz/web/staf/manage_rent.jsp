<%-- 
    Document   : manage_rent
    Created on : 22-may-2012, 0:37:34
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="tools.Tools"%>
<%@page import="model.Alquiler"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
%>

<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Gestión de Alquileres</title>
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
                        <h1>Gestión de Alquileres</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Acciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/newrent.jsp?st=1">Nuevo Alquiler</a></li>
                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% HashMap <String, Alquiler> alquileres = persistence.getAlquileres(null, null, emplLogedIn.getCodSucursal(), null);
                    if (alquileres != null){ %>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Alquileres disponibles</h1>
                        <table>
                            <tr class="theader"><td>Marca</td><td width="130" >Modelo</td><td width="170" >Cliente</td><td>F. Inicio</td><td>F. Fin</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <% for (Alquiler alq: alquileres.values()){ %>
                            <tr>
                                <td><%= alq.getVehiculo().getMarca() %></td>
                                <td><%= alq.getVehiculo().getModelo() %></td>
                                <td><%= alq.getCliente().getName() %></td>
                                <td><%= Tools.printDate(alq.getFechaInicio()) %></td>
                                <% if (alq.getFechaEntrega() == null) { %>
                                <td title="Entrega Prevista"><b>Entrega prevista</b><br /><%= Tools.printDate(alq.getFechaFin()) %></td>
                                <td><a title="Finalizar Alquiler" href="/staf/endrent.jsp?rent=<%= alq.getCodAlquiler() %>"><img alt="fin" src="/images/icons/endRent.png"/></a></td>
                                <% }else{ %>
                                <td title="Vehículo Entregado"><b>Entregado</b><br /><%= Tools.printDate(alq.getFechaEntrega()) %></td>
                                <td><img title="Alquiler Finalizado" alt="fin" src="/images/icons/ok.png"/></td>
                                <% } %>
                                <td><a title="Detalles Alquiler" href="/staf/viewrent.jsp?rent=<%= alq.getCodAlquiler() %>"><img alq="verAlq" src="/images/icons/viewRent.png"/></a></td>
                            </tr>
                            <% } %>
                        </table>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% } else{ %>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Alquileres disponibles</h1>
                        <blockquote class="exclamation">
                            <p>No se han encontrado alquileres en esta sucursal</p>
                        </blockquote>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% } %>
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

