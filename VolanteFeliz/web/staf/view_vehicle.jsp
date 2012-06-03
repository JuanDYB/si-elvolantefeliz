<%-- 
    Document   : view_vehicle
    Created on : 30-may-2012, 20:09:28
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Vehiculo"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Vehiculo ve = persistence.getVehiculo("codVehiculo", request.getParameter("v"), null);
    Sucursal suc = (Sucursal) session.getAttribute("sucursal");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Detalles del vehículo</title>
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

                    <% if (ve != null && (suc.getCodSucursal().equals(ve.getCodSucursal()) || suc.isCentral())) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles del vehículo</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <p><img alt="client" class="floatRight" src="/images/icons/carIcon.png"/></p>
                        <ul>
                            <li><b>Marca: </b><%= ve.getMarca() %></li>
                            <li><b>Modelo: </b><%= ve.getModelo() %></li>
                            <li><b>Matrícula: </b><%= ve.getMatricula() %></li>
                            <li><b>Número de Bastidor: </b><%= ve.getnBastidor() %></li>
                            <li><b>Capacidad de Combustible: </b><%= ve.getCapacidadCombustible() %> Litros</li>
                        </ul>
                    </div>
                            <% if (Tools.existeArchivo(application.getRealPath("/staf/vehicle_images/" + ve.getRutaImagen()))){ %>
                        <div class="gradient">
                            <h1>Imagen del Vehículo</h1>
                            <p><img class="floatLeft" width="98%" src ="/staf/vehicle_images/<%= ve.getRutaImagen() %>" alt="<%= ve.getRutaImagen() %>"></p>
                        </div>
                        <% } %>

                    <% } else if (ve != null && !suc.getCodSucursal().equals(ve.getCodSucursal()) && !suc.isCentral()) {%>
                    <div class="gradient">
                        <h1>Detalles Cliente</h1>
                        <blockquote class="exclamation">
                            <p>
                                No puede ver este vehículo porque no pertence a este sucursal y tampoco está en la sucursal central
                            </p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <h1>Detalles Cliente</h1>
                        <blockquote class="exclamation">
                            <p>
                                No se ha encontrado el vehículo seleccionado
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
        if (request.getParameterMap().size() >= 1 && request.getParameter("v") != null) {
            try {
                Tools.validateUUID(request.getParameter("v"));
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
