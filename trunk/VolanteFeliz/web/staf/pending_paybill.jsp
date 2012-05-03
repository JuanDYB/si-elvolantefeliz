<%-- 
    Document   : pending_paybill
    Created on : 
    Author     : Juan José Olivares
--%>

<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Factura"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (validateEntry(request) == false) {
        response.sendError(404);
        return;
    }
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Zona de empleados</title>
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
                        <h1>Pagar facturas pendientes</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <%
                            HashMap<String, Factura> facturasSinPagar = persistence.getFacturasSinPagar();
                        %>

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
