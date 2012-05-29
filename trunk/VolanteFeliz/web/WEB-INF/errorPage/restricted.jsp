<%-- 
    Document   : restricted
    Created on : 07-abr-2012, 11:25:38
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
        <meta http-equiv="Refresh" content="3; url=/login.jsp" />
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>&Aacute;rea Restringida</title>
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
                        <h1>Intento de entrada en area restringida</h1>
                        <p>Ser&aacute; redirigido a la <a href="/login.jsp">p&aacute;gina de login</a> autom&aacute;ticamente en 3 segundos</p>
                        <p style="text-align: center"><img src="/images/icons/lock.png" alt="prohibido" /></p>
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
