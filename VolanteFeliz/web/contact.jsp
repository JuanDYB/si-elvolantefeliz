<%-- 
    Document   : Contact
    Created on : 
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="java.util.HashMap"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Ayuda</title>
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
                        <h1>El Volante Feliz - Contacto</h1>
                        <p>
                            A continuación puede encontrar un listado de las sucursales de la empresa con los datos de las mismas, 
                            de esta manera podrá ponerse en contacto directamente con la que desee
                        </p>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% HashMap <String, Sucursal> sucursales = persistence.getSucursales(false);
                    if (sucursales != null){ %>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Listado de Sucursales</h1>
                        <% for (Sucursal suc : sucursales.values()){ %>
                        <hr />
                        <h2><%= suc.getNombre() %></h2>
                        <ul>
                            <li><b>Direcci&oacute;n: </b><%= suc.getDir()%></li>
                            <li><b>Teléfono: </b><%= suc.getTelefono()%></li>
                            <li><b>Fax: </b><%= suc.getFax()%></li>
                        </ul>
                        <% } %>
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

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = "class=\"here\""; %>
