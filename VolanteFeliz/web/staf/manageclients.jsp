<%-- 
    Document   : manageclients
    Created on : 12-abr-2012, 11:35:09
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
        <title>Gesti&oacute;n de Clientes</title>
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
                        <h1>Gestión de Clientes</h1>
                        <h2>Operaciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/newclient.jsp">Dar de alta nuevo cliente</a></li>
                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Clientes disponibles</h1>
                        <p>
                            A continuación se muestra una tabla con los clientes pertenecientes a la sucursal
                            , dentro de la tabla podrá realizar las acciones que considere necesarias con un determinado cliente
                        </p>
                        
                        <table border="0" align="center" width="90%" >
                            
                            
                        </table>
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
<%! String menuPreferencias = "class=\"here\""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
