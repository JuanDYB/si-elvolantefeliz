<%-- 
    Document   : new_vehicle
    Created on : 30-may-2012, 20:33:50
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.TipoITV"%>
<%@page import="model.TipoRevision"%>
<%@page import="model.TipoVehiculo"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    HashMap <String, TipoVehiculo> tiposVehiculos = persistence.getTiposVehiculo();
    HashMap <String, TipoRevision> tiposRevisiones = persistence.getTiposRevision();
    HashMap <String, TipoITV> tiposITV = persistence.getTiposITV();
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />

        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Nuevo vehiculo</title>
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
                        <h1>Gestión de vehículos</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Alta nuevo vehículo</h2>
                        <%  if (tiposITV != null && tiposRevisiones != null && tiposVehiculos != null) {%>
                        <p>
                            Puede usar este formulario para dar de alta un nuevo vehículo en el sistema
                        </p>
                        <form name="newvehicle" method="POST" action="/staf/administration/new_vehicle">
                            <p>
                                <label>Matricula</label>
                                <input name="matricula" type="text" size="30" maxlength="20" class=":matricula :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Marca</label>
                                <input name="marca" type="text" size="50" maxlength="45" class=":marca :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Modelo</label>
                                <input name="modelo" type="text" size="50" maxlength="45" class=":modelo :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Número de bastidor</label>
                                <input name="nBastidor" type="text" size="25" maxlength="17" class=": :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Capacidad de combustible (Litros)</label>
                                <input name="combustible" type="text" size="30" maxlength="10" class=":digits :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Tipo de Vehículo</label>
                                <select name="tipoVehiculo" class=":required">
                                    <% for (TipoVehiculo tipoVehiculo : tiposVehiculos.values()){ %>
                                    <option value="<%= tipoVehiculo.getCodTipoVehiculo() %>"><%= tipoVehiculo.getNombre() %></option>
                                    <% } %>
                                </select>
                            </p>
                            <p>
                                <label>Tipo de ITV</label>
                                <select name="tipoITV" class=":required">
                                    <% for (TipoITV tipoITV : tiposITV.values()){ %>
                                    <option value="<%= tipoITV.getCodTipoITV() %>"><%= tipoITV.getNombre() %></option>
                                    <% } %>
                                </select>
                            </p>
                            <p>
                                <label>Tipo de Revisión</label>
                                <select name="tipoRev" class=":required">
                                    <% for (TipoRevision tipoRev : tiposRevisiones.values()){ %>
                                    <option value="<%= tipoRev.getCodTipoRevision() %>"><%= tipoRev.getNombre() %></option>
                                    <% } %>
                                </select>
                            </p>
                            
                            <p>
                                <input name="send" type="submit" value="Confirmar datos vehículo" />
                            </p>
                        </form>
                        <% } else if (tiposITV == null) {%>
                        <blockquote class="exclamation">
                            <p>No se puede dar de alta un vehículo, no existen tipos de ITV</p>
                        </blockquote>
                        <% } else if (tiposRevisiones == null) { %>
                        <blockquote class="exclamation">
                            <p>No se puede dar de alta un vehículo, no existen tipos de revisiones</p>
                        </blockquote>
                        <% } else { %>
                        <blockquote class="exclamation">
                            <p>No se puede dar de alta un vehículo, no existen tipos de vehículos</p>
                        </blockquote>
                        <% } %>
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

