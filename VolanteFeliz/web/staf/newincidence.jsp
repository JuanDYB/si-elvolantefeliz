<%-- 
    Document   : newincidence
    Created on : 25-may-2012, 17:11:38
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.TipoIncidencia"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
HashMap <String, TipoIncidencia> tiposIncidencias = persistence.getTiposIncidencia();
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.7.2.custom.css" />
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>

        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <%@include file="/WEB-INF/include/calendar.jsp" %>
        <title>Nueva Incidencia</title>
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
                    <% if (tiposIncidencias != null){ %>
                    <div class="gradient">
                        <h1>Nueva Incidencia</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <form method="POST" action="/staf/newincidence" name="newIncidence">
                            <p>
                                <label>Fecha de la incidencia</label>
                                <input type="text" name="fechainc" id="fechainc" readonly="readonly" size="12" class=":date_au :required :only_on_submit" />
                            </p>
                            <p>
                                <label>Matrícula del Vehículo</label>
                                <input type="text" name="matricula" size="30" maxlength="20" class=":matricula :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Seleccione tipo de incidencia</label>
                                <select name="incType">
                                    <% for (TipoIncidencia incType : tiposIncidencias.values()){ %>
                                    <option title="<%= incType.getDescripcion() %>" value="<%= incType.getCodTipoIncidencia() %>"><%= incType.getNombre() %></option>
                                    <% } %>
                                </select>
                            </p>
                            <p>
                                <label>Introduzca un precio para la incidencia (si procede)</label>
                                <input type="text" name="precio" size="20" maxlength="15" class=":required :number :only_on_blur" /> 
                            </p>
                            <p>
                                <label>Observaciones</label>
                                <textarea name="observaciones" cols="60" rows="15" class=":required :only_on_blur" ></textarea>
                            </p>
                            <p>
                                <input type="submit" value="Continuar >>" name="newinc" />
                            </p>
                        </form>
                    </div>
                    <% }else{ %>
                    <div class="gradient">
                        <h1>Nueva incidencia</h1>
                        <blockquote class="exclamation">
                            <p>No se puede dar de alta una nueva incidencia porque no hay tipos de incidencia dados de alta en la base de datos</p>
                        </blockquote>
                    </div>
                    <% } %>
                    
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
