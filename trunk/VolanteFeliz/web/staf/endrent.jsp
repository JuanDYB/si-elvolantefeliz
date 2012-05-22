<%-- 
    Document   : endrent
    Created on : 22-may-2012, 14:19:00
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="java.util.Date"%>
<%@page import="model.Alquiler"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    if (!this.validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Alquiler alq = persistence.getAlquiler(request.getParameter("rent"));
%>

<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        
        <script type="text/javascript" src="/scripts/tiny_mce/tiny_mce.js"></script>
        <script type="text/javascript" src="/scripts/editor.js"></script>
        
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        
        
        <title>Finalizar Alquiler</title>
    </head>
    <body onload="loadEditor();">
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
                    <%if (alq != null && alq.getFechaEntrega() == null) {%>
                    <div class="gradient">
                        <h1>Finalizar Alquiler</h1>
                        <% Date today = Tools.getDate(); %>
                        <ul>
                            <li><b>Fecha de Entrega: </b><%= Tools.printDate(today) %></li>
                        </ul>
                        <h2>Datos de finalización</h2>
                        <form name="endrent" action="/staf/endrent" method="POST">
                            <p>
                                <label>Introduzca los kilómetros finales</label>
                                <input type="text" name="KMFin" size="30" maxlength="10" class=":digits :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Combustible Final (Lleno: <%= alq.getVehiculo().getCapacidadCombustible() %> Litros)</label>
                                <input type="text" name="combustible_fin" size="30" maxlength="10" class=":digits :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Observaciones</label>
                                <textarea name="observaciones" cols="60" rows="15"></textarea>
                            </p>
                            <p>
                                <input type="hidden" name="rent" value="<%= alq.getCodAlquiler() %>" />
                                <input type="hidden" name="date" value="<%= today.getTime() %>" />
                                <input type="submit" name="endrent" value="Confirmar fin de alquiler" />
                            </p>
                        </form>
                    </div>
                    <% } else if (alq != null) {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>Este alquiler ya tiene una fecha de entrega asignada, ya ha sido finalizado</p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="stop">
                            <p>No se ha encontrado el alquiler seleccionado. Disculpe las molestias</p>
                        </blockquote>
                    </div>
                    <% }%>
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

<%!
    private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("rent") != null) {
            try {
                Tools.validateUUID(request.getParameter("rent"));
                return true;
            } catch (ValidationException ex) {
                return false;
            }
        }
        return false;
    }
%>