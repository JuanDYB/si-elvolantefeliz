<%-- 
    Document   : viewclient
    Created on : 16-abr-2012, 19:02:32
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="tools.Tools"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    if (!validateForm(request)){
        response.sendError(404);
        return;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Detalles cliente</title>
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
                        <h1>Detalles Cliente</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <!-- INSERTAR AQUI DETALLES DEL CLIENTE, VARIOS BLOQUES GRADIENTE PARA LOS APARTADOS, o pestañas JS... -->
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

<%! private boolean validateForm (HttpServletRequest request){
    if (request.getParameterMap().size() >= 1 && request.getParameter("cli") != null){
        try{
            Tools.validateUUID (request.getParameter("cli"));
            return true;
        } catch (ValidationException ex){
            return false;
        }
    }
    return false;
}
%>

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"here\""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
