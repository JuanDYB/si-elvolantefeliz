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
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
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
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Informaci&oacute;n General de la sucursal</h2>
                        <p><img alt="icono suc" class="floatRight" src="/images/icons/iconoSucursal.png"/></p>
                        <ul>
                            <li><b>Sucursal: </b><%= suc.getNombre()%></li>
                            <li><b>Teléfono: </b><%= suc.getTelefono() %></li>
                            <li><b>Fax: </b><%= suc.getFax() %></li>
                            <li><b>Direcci&oacute;n: </b><%= suc.getDir() %></li>
                            <% if (suc.isCentral()){ %>
                            <li><b>Sucursal Central: </b>Si</li>
                            <% }else{ %>
                            <li><b>Sucursal Central: </b>No</li>
                            <% } %>
                        </ul>
                        <br /><br />
                        <h2>Informaci&oacute;n General del Empleado</h2>
                        <p><img alt="icono empl" class="floatRight" src="/images/icons/iconoEmpleado.png"/></p>
                        <ul>
                            <li><b>Nombre: </b><%= emplLogedIn.getName() %></li>
                            <li><b>Nombre de Usuario: </b><%= emplLogedIn.getUserName() %></li>
                            <li><b>DNI: </b><%= emplLogedIn.getDni() %></li>
                            <li><b>Dirección: </b><%= emplLogedIn.getAddress() %></li>
                            <li><b>Tel&eacute;fono: </b><%= emplLogedIn.getTelephone() %></li>
                            <% if (emplLogedIn.getPermisos() == 'a'){ %>
                            <li><b>Categoría </b>Administrador</li>
                            <% } else if (emplLogedIn.getPermisos() == 'e'){ %>
                            <li><b>Categoría: </b>Empleado</li>
                            <% } %>
                        </ul>
                        
                        <p style="text-align: center;">
                        <a title="Manual de usuario" href="/staf/ManualUsuario.pdf">
                            <img src="/images/icons/userManual.png"  alt="manual usuario" /></a>
                        </p>
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
