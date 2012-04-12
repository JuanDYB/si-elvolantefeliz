<%-- 
    Document   : newclient
    Created on : 06-abr-2012, 11:21:40
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
        <title>Nuevo Cliente</title>
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
                        <h1>Gesti&oacute;n de Clientes</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Alta de Nuevo Cliente</h2>
                        <p>
                            Puede usar el formulario que puede encontrar a continuación para dar de alta a nuevo cliente en el sistema.
                            <br />Es necesario que un cliente este registrado en el sistema para poder alquilar vehículos
                        </p>
                        <form name="newclient" method="POST" action="/staf/newclient">
                            <p>
                                <label>Nombre</label>
                                <input name="name" type="text" size="70" maxlength="200" />
                            </p>
                            <p>
                                <label>DNI</label>
                                <input name="dni" type="text" size="10" maxlength="9" />
                            </p>
                            <p>
                                <label>Direcci&oacute;n</label>
                                <input name="address" type="text" size="70" maxlength="400" />
                            </p>
                            <p>
                                <label>Teléfono</label>
                                <input name="phone" type="text" size="20" maxlength="14"/>
                            </p>
                            <p>
                                <label>Empresa</label>
                                <input name="company" type="text" size="70" maxlength="100"/>
                            </p>
                            <p>
                                <label>Email</label>
                                <input name="email" type="text" size="70" maxlength="100"/>
                            </p>
                            <p>
                                <label>Edad</label>
                                <input name="age" type="text" size="5" maxlength="3"/>
                            </p>
                            <p>
                                <input name="send" type="submit" value="Confirmar Alta" />
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

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"here\""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>