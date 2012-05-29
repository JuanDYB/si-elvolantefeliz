<%-- 
    Document   : login
    Created on : 06-abr-2012, 11:21:59
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="tools.WebConfig"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />

        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Login</title>
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
                        <h1>Acceso al &Aacute;rea de Administraci&oacute;n</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Inicio de Sesi&oacute;n</h2>
                        <% WebConfig appConfig = (WebConfig) application.getAttribute("appConfig");
                        int intentosMax = appConfig.getMaxLoginAttempt();
                        if (session.getAttribute("login") != null && session.getAttribute("empleado") != null 
                        && (Boolean) session.getAttribute("login")){ %>
                        <blockquote class="exclamation">
                            <ul>
                                <li>No puede inciar sesión porque ya tiene una sesión iniciada</li>
                                <li>Si lo desea puede <a href="/logout">cerrar la sesi&oacute;n</a> para iniciar con un usuario diferente</li>
                            </ul>
                        </blockquote>
                        <% } else if (session.getAttribute("intentosLogin") != null 
                                && ((Integer)session.getAttribute("intentosLogin")) >= intentosMax){ %>
                                <blockquote class="exclamation">
                                    <p>Intentos de inicio de sesi&oacute;n agotados. Puede intentarlo de nuevo pasados unos minutos</p>
                                </blockquote>
                                <p style="text-align: center"><img src="/images/icons/lock.png" alt="prohibido" /></p>
                        <% } else{ %>
                        <form name="login" method="POST" action="/login">
                            <p>
                                <label>Nombre de usuario</label>
                                <input name="userName" type="text" size="50" maxlength="20" class=":user_name :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Contrase&ntilde;a</label>
                                <input name="pass" type="password" size="50" maxlength="50" class=":password :required :only_on_blur" />
                            </p>
                            <p>
                                <input name="login" type="submit" value="Iniciar Sesi&oacute;n" />
                            </p>
                        </form>
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

<%! String menuInicio = ""; %>
<%! String menuLogin = "class=\"here\""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
