<%-- 
    Document   : manage_suc
    Created on : 27-may-2012, 12:45:39
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="java.util.HashMap"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Gestión de sucursales</title>
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
                        <h1>Gestión de sucursales</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <%--
                        <h2>Acciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/administration/new_employee.jsp">Nuevo empleado</a></li>
                        </ul> --%>
                        <% HashMap <String, Sucursal> sucursales = persistence.getSucursales(false);
                        if (sucursales != null){ %>
                        <h2>Sucursales disponibles</h2>
                        <p><b>NOTA: </b>Si hay una sucursal central aparecerá marcada con un <i>tick</i></p>
                        <table>
                            <tr class="theader"><td>Nombre</td><td>Teléfono</td><td>Fax</td><td>Dirección</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <% for (Sucursal suc: sucursales.values()){ %>
                            <tr>
                                <td><%= suc.getNombre() %></td>
                                <td><%= suc.getTelefono() %></td>
                                <td><%= suc.getFax() %></td>
                                <td><%= suc.getDir() %></td>
                                <td><a title="Detalles sucursal" href="/staf/administration/viewsuc.jsp?suc=<%= suc.getCodSucursal() %>"><img src="/images/icons/view_suc.png" alt="ver sucursal" /></a></td>
                                <% if (suc.isCentral()) { %>
                                <td><img title="Sucursal central" src="/images/icons/ok.png" alt="suc central" /></td>
                                <% }else{ %>
                                <td>&nbsp;</td>
                                <% } %>
                                <td><a title="Ver empleados" href="/staf/administration/manage_empl.jsp?suc=<%= suc.getCodSucursal() %>">
                                        <img src="/images/icons/view_empl.png" alt="ver empleados" /></a>
                                </td>
                                
                            </tr>
                            <% } %>
                        </table>
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
