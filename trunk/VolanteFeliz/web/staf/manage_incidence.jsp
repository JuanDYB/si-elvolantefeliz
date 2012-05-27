<%-- 
    Document   : manage_incidence
    Created on : 27-may-2012, 16:20:48
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="tools.Tools"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Incidencia"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");    
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
    HashMap <String, Incidencia> incidencias = null;
    if (request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()){
        incidencias = persistence.getIncidencias(null, null, null);
    }else{
        incidencias = persistence.getIncidencias(null, null, emplLogedIn.getCodSucursal());
    }
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Gestión Incidencias</title>
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

                    <% if (incidencias != null && 
                    (request.getParameter("all") == null || 
                    (request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()))) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Gestión Incidencias</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Acciones Disponibles</h2>
                        <ul>
                            <li><a href="/staf/newincidence.jsp">Nueva Incidencia</a></li>
                            <% if (suc.isCentral()){ %>
                            <li><a href="/staf/manage_incidence.jsp?all=1"><b>Sucursal Central: </b>Ver todas las incidencias</a></li>
                            <% } %>
                        </ul>
                        <h2>Listado de Incidencias</h2>
                        <table>
                            <tr class="theader"><td>Observaciones</td><td>Fecha</td><td>Importe</td><td>&nbsp;</td></tr>
                            <% for (Incidencia inc : incidencias.values()) { %>
                            <tr>
                                <td><b><%= inc.getTipoIncidencia().getNombre()%>: </b><%= inc.getObservaciones()%></td>
                                <td><%= Tools.printDate(inc.getFecha())%></td>
                                <td><%= Tools.printBigDecimal(inc.getPrecio())%> €</td>
                                <td><a title="Detalles Incidencia"  href="/staf/viewincidence.jsp?inc=<%= inc.getCodIncidencia() %>"><img src="/images/icons/view_incidence.png" alt="ver incidencia" /></a></td>
                            </tr>
                            <% }%>
                        </table>
                    </div>
                        <% } else if (incidencias != null && request.getParameter("all") != null 
                        && request.getParameter("all").equals("1") && !suc.isCentral()) {%>
                    <div class="gradient">
                        <h1>Gestión Incidencias</h1>
                        <blockquote class="exclamation">
                            <p>
                                No puede ver todos las incidencias de todas las sucursales porque no esta en una sucursal central
                            </p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No se han encontrado incidencias en el sistema
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


<%! String menuInicio = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"here\"";%>
<%! String menuAbout = "";%>
<%! String menuContacto = "";%>
