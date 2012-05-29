<%-- 
    Document   : about
    Created on : 15-abr-2012, 12:32:32
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Acerca de</title>
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
                        <h1>Acerca de</h1>
                        <h2>¿Quién ha construido este Sistema de Información?</h2>
                        <h3>Eiffel &amp; Cibeles Software S.L</h3>
                        <p><img alt="code" class="floatRight" src="/images/icons/xcode.png"/></p>
                        <ul>
                            <li><b>Jefe de Proyecto: </b>Juan Díez-Yanguas Barber</li>
                            <li><b>Analista y Programador: </b>Juan Díez-Yanguas Barber</li>
                            <li><b>Equipo de test</b>
                                <ul>
                                    <li>Tiphanie Dousset</li>
                                    <li>Florent Quilichini</li>
                                </ul>
                            </li>
                        </ul>
                        <br />
                        <h2>¿Cuál es el fin de este Sistema de Información?</h2>
                        <p>
                            Este Sistema proporciona todas las facilidades necesarias para la gestión de una empresa de alquiler de vehículos.
                            <br />
                            La aplicación será capaz de gestionar los vehículos, sucursales, empleados, facturación, clientes, etc...
                        </p>
                        <br />
                        <h2>Requisitos necesarios</h2>
                        <ul>
                            <li>Servidor de bases de datos MySQL</li>
                            <li>Servidor SMTP de correo</li>
                            <li>Servidor de aplicaciones GlassFish</li>
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

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = "class=\"here\""; %>
<%! String menuContacto = ""; %>
