<%-- 
    Document   : index
    Created on : 23-mar-2012, 10:08:22
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Inicio</title>
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
                <div class="width25 floatLeft leftColumn">
                    <h1>Intro</h1>

                    <ul class="sideMenu">
                        <li class="here">
                            Dynamic Template
                            <ul>
                                <li><a href="#fluidity" title="Jump to section">Book of Fluidity</a></li>
                                <li><a href="#coding" title="Jump to section">Coding</a></li>
                            </ul>
                        </li>
                        <li><a href="http://fullahead.org" title="Goto Fullahead">Fullahead</a></li>
                        <li><a href="http://threetree.net" title="Goto Threetree">ThreeTree</a></li>
                    </ul>

                </div>
                <!-- FIN COLUMNA IZQUIERDA -->

                <!-- Columna principal -->
                <div class="width75 floatRight">


                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>El Volante Feliz</h1>
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

<%! String menuInicio = "class=\"here\""; %>
<%! String menuLogin = ""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
