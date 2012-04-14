<%-- 
    Document   : index.jsp
    Created on : 06-abr-2012, 11:58:19
    Author     : JuanDYB
--%>

<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Zona de administraci&oacute;n</title>
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
                        <h1>El Volante Feliz - Zona de administraci&oacute;n</h1>
                        <h2>Informaci&oacute;n General de la sucursal</h2>
                        <% if (suc != null) {%>
                        <ul>
                            <li><b>Sucursal: </b><%= suc.getNombre()%></li>
                            <li><b>Nombre: </b><%= suc.getDir() %></li>
                            <li><b>Teléfono: </b><%= suc.getTelefono() %></li>
                            <li><b>Fax: </b><%= suc.getFax() %></li>
                            <li><b>Direcci&oacute;n: </b><%= suc.getDir() %></li>
                        </ul>
                        <% } else {%>
                        <blockquote class="stop">
                            <p>
                                Ha ocurrido un error obteniendo la sucursal al que pertenece el empleado
                            </p>
                        </blockquote>
                        <% }%>
                        <h2>Informaci&oacute;n General del Empleado</h2>
                        <ul>
                            <li><b>Nombre: </b><%= emplLogedIn.getName() %></li>
                            <li><b>Nombre de Usuario: </b><%= emplLogedIn.getUserName() %></li>
                            <li><b>DNI: </b><%= emplLogedIn.getDni() %></li>
                            <li><b>Dirección: </b><%= emplLogedIn.getAddress() %></li>
                            <li><b>Tel&eacute;fono: </b><%= emplLogedIn.getTelephone() %></li>
                            <% if (emplLogedIn.getPermisos() == 'a'){ %>
                            <li><b>Categoría </b>Empleado</li>
                            <% } else if (emplLogedIn.getPermisos() == 'e'){ %>
                            <li><b>Categoría: </b>Administrador</li>
                            <% } %>
                        </ul>
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
