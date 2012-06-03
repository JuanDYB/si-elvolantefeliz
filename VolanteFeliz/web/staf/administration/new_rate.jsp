<%-- 
    Document   : new_rate
    Created on : 03-jun-2012, 23:27:52
    Author     : Lourdes Gómez Rincón
--%>

<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = (Sucursal) session.getAttribute("sucursal");
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />

        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Nueva Tarifa</title>
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
                        <h1>Nueva Tarifa</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Alta nuevo vehículo</h2>
                        <p>
                            Puede usar este formulario para dar de alta una tarifa en el sistema
                        </p>
                        <form name="newrate" method="POST" action="/staf/administration/new_rate">
                            <p>
                                <label>Nombre</label>
                                <input name="matricula" type="text" size="30" maxlength="20" class=":required :only_on_blur" />
                            </p>
                        </form>
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

