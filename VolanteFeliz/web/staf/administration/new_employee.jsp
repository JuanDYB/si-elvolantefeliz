<%-- 
    Document   : new_employee
    Created on : 20-may-2012, 12:40:39
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="java.util.HashMap"%>
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

        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Nuevo Empleado</title>
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
                        <h1>Gestión de Personal</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Alta de Nuevo Empleado</h2>
                        <% HashMap<String, Sucursal> sucursales = persistence.getSucursales(false);
                            if (sucursales != null) {%>
                        <p>
                            Puede usar el formulario que puede ver a continuación para dar de alta un nuevo empleado en el sistema
                        </p>
                        <form name="newempl" method="POST" action="/staf/administration/newempl">
                            <p>
                                <label>Nombre</label>
                                <input name="name" type="text" size="70" maxlength="200" class=":name :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Nombre de usuario</label>
                                <input name="userName" type="text" size="70" maxlength="50" class=":user_name :required :only_on_blur" />
                            </p>
                            <p>
                                <label>DNI</label>
                                <input name="dni" type="text" size="10" maxlength="9" class=":DNI :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Contraseña de acceso</label>
                                <input id="pass" name="pass" type="password" size="50" maxlength="50" class=":password :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Repita la contraseña de acceso</label>
                                <input name="pass_rep" type="password" size="50" maxlength="50" class=":same_as;pass :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Teléfono</label>
                                <input name="phone" type="text" size="20" maxlength="14" class=":tlf :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Direcci&oacute;n</label>
                                <label>Ejemplo: Calle, 1 28002-Madrid</label>
                                <input name="address" type="text" size="70" maxlength="400" class=":dir :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Sucursal</label>
                                Debe seleccionar una sucursal a la que el nuevo empleado quedará asignado<br />
                            <select name="suc" class=":required :only_on_blur">
                                <% for (Sucursal suc : sucursales.values()) {%>
                                <option value="<%= suc.getCodSucursal()%>"><%= suc.getNombre()%></option>
                                <% }%>
                            </select>
                            </p>

                            <p><label>Permisos de acceso</label>
                                <select name="perm" class=":required :only_on_blur">
                                    <option value="e">Empleado</option>
                                    <option value="a">Administrador</option>
                                </select></p>
                            <p>
                                <input name="send" type="submit" value="Confirmar Alta Empleado" />
                            </p>
                        </form>
                        <% } else {%>
                        <blockquote class="exclamation">
                            <p>No se puede añadir un nuevo empleado porque no se han encontrado sucursales en el sistema</p>
                        </blockquote>
                        <% }%>
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
