<%-- 
    Document   : manage_empl
    Created on : 20-may-2012, 16:47:45
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    HashMap <String, Empleado> empleados = null;
    Sucursal sucPedida = null;
    if (request.getParameter("suc") != null){
        try{
            Tools.validateUUID(request.getParameter("suc"));
            sucPedida = persistence.getSucursal(request.getParameter("suc"));
            if (sucPedida != null){
                empleados = persistence.getEmpleados("codSucursal", sucPedida.getCodSucursal());
            }
        }catch(ValidationException ex){
            response.sendError(404);
        }
    }else{
        empleados = persistence.getEmpleados(null, null);
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Nuevo Empleado</title>
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
                        <h1>Gestión de Personal</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Acciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/administration/new_employee.jsp">Nuevo empleado</a></li>
                        </ul>
                        <% if (request.getParameter("suc") != null && sucPedida != null && empleados != null){ %>
                        <h2>Empleados disponibles para la sucursal: <%= sucPedida.getNombre() %></h2>
                        <table>
                            <tr class="theader"><td>Nombre</td><td>DNI</td><td>Teléfono</td><td>Tipo</td><td>Sucursal</td></tr>
                            <% for (Empleado empl: empleados.values()){ %>
                            <tr>
                                <td><%= empl.getName() %></td>
                                <td><%= empl.getDni() %></td>
                                <td><%= empl.getTelephone() %></td>
                                <% if (empl.getPermisos() == 'a'){ %>
                                <td>Administrador</td>
                                <% }else if (empl.getPermisos() == 'e') { %>
                                <td>Empleado</td>
                                <% } %>
                                <td><a title="Ver Sucursal" href="/staf/administration/viewsuc.jsp?suc=<%= empl.getCodSucursal() %>">
                                        <img src="/images/icons/view_suc.png" alt="ver sucursal" /></a>
                                </td>
                            </tr>
                            <% } %>
                        </table>
                        <% }else if (request.getParameter("suc") != null && sucPedida != null && empleados == null){ %>
                        <h2>Empleados disponibles para la sucursal: <%= sucPedida.getNombre() %></h2>
                        <blockquote class="exclamation" >
                            <p>No se han encontrado empleados asignados a la sucursal selecionada</p>
                        </blockquote>
                        <% }else if (request.getParameter("suc") != null && sucPedida == null) { %>
                        <h2>Empleados disponibles</h2>
                        <blockquote class="stop" >
                            <p>La sucursal seleccionada no se ha encontrado</p>
                        </blockquote>
                        <% }else if (request.getParameter("suc") == null && empleados != null){ %>
                        <h2>Empleados disponibles</h2>
                        <table>
                            <tr class="theader"><td>Nombre</td><td>DNI</td><td>Teléfono</td><td>Tipo</td><td>&nbsp;</td></tr>
                            <% for (Empleado empl: empleados.values()){ %>
                            <tr>
                                <td><%= empl.getName() %></td>
                                <td><%= empl.getDni() %></td>
                                <td><%= empl.getTelephone() %></td>
                                <% if (empl.getPermisos() == 'a'){ %>
                                <td>Administrador</td>
                                <% }else if (empl.getPermisos() == 'e') { %>
                                <td>Empleado</td>
                                <% } %>
                                <td><a title="Ver Sucursal" href="/staf/administration/viewsuc.jsp?suc=<%= empl.getCodSucursal() %>">
                                        <img src="/images/icons/view_suc.png" alt="ver sucursal" /></a>
                                </td>
                            </tr>
                            <% } %>
                        </table>
                        <% }else{ %>
                        <h2>Empleados disponibles</h2>
                        <blockquote class="exclamation" >
                            <p>No se han encontrado empleados</p>
                        </blockquote>
                        <% } %>
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

<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
