<%-- 
    Document   : error500
    Created on : 19-may-2012, 15:44:37
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
        <title>Error interno</title>
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
                        <h1>Error 500 - Error interno</h1>
                        <p>Ha ocurrido un error interno en el servidor, disculpe las molestias</p>
                        <p>Si lo desea puede <a href="/contact.jsp">contactar</a> con nosotros para comunicarnos el error</p>
                        <p>Puede volver al <a href="/index.jsp">inicio</a> si lo desea</p>
                        <p style="text-align: center"><img src="/images/icons/serverError.png" alt="prohibido" /></p>
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

<%! String menuInicio = "class=\"here\"";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
