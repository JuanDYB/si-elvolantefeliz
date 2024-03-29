<%-- 
    Document   : logout
    Created on : 06-abr-2012, 10:55:00
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <meta http-equiv="Refresh" content="3; url=/index.jsp" />
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Logout</title>
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
                        <h1>Cerrar Sesi&oacute;n</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <p><img alt="logout" class="floatRight" src="/images/icons/logoutIcon.png"/></p>
                        <h2>Sesi&oacute;n cerrada correctamente</h2>
                        <p>
                            Sera redirigido en 3 segundos a la p&aacute;gina principal, si no se redirige autom&aacute;ticamente haga click <a href="/index.jsp">aqu&iacute;</a>
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

<%! String menuInicio = ""; %>
<%! String menuLogin = "class=\"here\""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
