<%-- 
    Document   : viewincidence
    Created on : 27-may-2012, 16:01:56
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Incidencia"%>
<%@page import="tools.Tools"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Incidencia inc = persistence.getIncidencia("codIncidencia", request.getParameter("inc"));
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Detalles Incidencia</title>
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

                    <% if (inc != null) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles Incidencia</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        
                        <ul>

                            <li><h3>Tipo de Incidencia: </h3>
                                <ul>
                                    <li><b>Nombre: </b><%= inc.getTipoIncidencia().getNombre() %></li>
                                    <li><b>Descripción: </b><%= inc.getTipoIncidencia().getDescripcion() %></li>
                                    <% if (inc.getTipoIncidencia().isAbonaCliente()){ %>
                                    <li><b>A Abonar por el cliente: </b>Si</li>
                                    <% } else { %>
                                    <li><b>A Abonar por el cliente: </b>No</li>
                                    <% } %>
                                </ul>
                            </li>
                            <li><b>Fecha: </b><%= Tools.printDate(inc.getFecha()) %></li>
                            <li><b>Importe: </b><%= Tools.printBigDecimal(inc.getPrecio()) %> €</li>
                            <li><b>Alquiler: </b><a href="/staf/viewrent.jsp?rent=<%= inc.getCodAlquiler() %>" >Ver Alquiler</a></li>
                            <li><b>Cliente: </b><a href="/staf/viewclient.jsp?cli=<%= inc.getCodCliente() %>" >Ver Cliente</a></li>
                        </ul>
                        <h3>Observaciones</h3>
                        <p><%= inc.getObservaciones() %></p>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No se ha encontrado el cliente seleccionado
                            </p>
                        </blockquote>
                    </div>
                    <% }%>
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

<%! private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("inc") != null) {
            try {
                Tools.validateUUID(request.getParameter("inc"));
                return true;
            } catch (ValidationException ex) {
                return false;
            }
        }
        return false;
    }
%>

<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
