<%-- 
    Document   : manage_vehicles
    Created on : 30-may-2012, 10:37:37
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="model.Vehiculo"%>
<%@page import="java.util.HashMap"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Sucursal suc = (Sucursal) session.getAttribute("sucursal");
    HashMap <String, Vehiculo> vehiculos = null;
    if (request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()){
        vehiculos = persistence.getVehiculos(null, null);
    }else{
        vehiculos = persistence.getVehiculos("codSucursal", emplLogedIn.getCodSucursal());
    }
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Gestión vehículos</title>
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
                        <h1>Acciones disponibles</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <ul>
                            <% if (suc.isCentral()){ %>
                            <li><a href="/staf/manage_vehicles.jsp?all=1">Ver todos los vehículos</a></li>
                            <% } %>
                            <li><a>Añadir nuevo vehículo</a></li>
                        </ul>
                    </div>
                    <% if (vehiculos != null && ((request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()) || (request.getParameter("all") == null) )){ %>
                    <div class="gradient">
                        <h1>Vehículos disponibles</h1>
                        <table>
                            <tr class="theader"><td>Marca</td><td>Modelo</td><td>Matrícula</td><td>&nbsp;</td></tr>
                            <% for (Vehiculo ve: vehiculos.values()){ %>
                            <tr>
                            <td><%= ve.getMarca() %></td>
                            <td><%= ve.getModelo() %></td>
                            <td><%= ve.getMatricula() %></td>
                            <td><a title="Ver detalles Vehículo" href="/staf/view_vehicle.jsp?v=<%= ve.getCodVehiculo() %>"><img src="/images/icons/viewCar.png" alt="ver coche" /></a></td>
                            </tr>
                            <% } %>
                        </table>
                    </div>
                    <% }else if (vehiculos != null && request.getParameter("all") != null && request.getParameter("all").equals("1") && !suc.isCentral()){ %>
                    <div class="gradient">
                        <h1>Vehículos disponibles</h1>
                        <blockquote class="exclamation">
                            <p>No puede ver todos los vehículos porque no esta en una sucursal central</p>
                        </blockquote>
                    </div>
                    <% }else{ %>
                    <div class="gradient">
                        <h1>Vehículos disponibles</h1>
                        <blockquote class="exclamation">
                            <p>No se han encontrado vehículos registrados</p>
                        </blockquote>
                    </div>
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
