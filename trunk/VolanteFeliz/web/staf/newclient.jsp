<%-- 
    Document   : newclient
    Created on : 06-abr-2012, 11:21:40
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        
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
                                <input name="name" type="text" size="70" maxlength="200" class=":name :required :only_on_blur" />
                            </p>
                            <p>
                                <label>DNI</label>
                                <input name="dni" type="text" size="10" maxlength="9" class=":DNI :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Direcci&oacute;n</label>
                                <label>Ejemplo: Calle, 1 28002-Madrid</label>
                                <input name="address" type="text" size="70" maxlength="400" class=":dir :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Teléfono</label>
                                <input name="phone" type="text" size="20" maxlength="14" class=":tlf :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Empresa</label>
                                <input name="company" type="text" size="70" maxlength="100" class=":name :only_on_blur" />
                            </p>
                            <p>
                                <label>Email</label>
                                <input name="email" type="text" size="70" maxlength="100" class=":email :required :only_on:blur"/>
                            </p>
                            <p>
                                <label>Edad</label>
                                <input name="age" type="text" size="5" maxlength="3" class=":age :required :only_on_blur"/>
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