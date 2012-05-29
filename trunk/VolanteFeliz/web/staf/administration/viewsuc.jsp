<%-- 
    Document   : viewsuc
    Created on : 27-may-2012, 1:12:03
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="tools.WebConfig"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Factura"%>
<%@page import="model.Incidencia"%>
<%@page import="model.Alquiler"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="tools.Tools"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Sucursal suc = persistence.getSucursal(request.getParameter("suc"));
    WebConfig webConfig = (WebConfig) application.getAttribute("appConfig");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <title>Detalles de Sucursal</title>
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
                    <% if (suc != null) {%>
                    <div class="gradient">
                        <h1>Detalles de Sucursal</h1>
                        <p><img alt="chart" class="floatRight" src="/images/icons/Chart_history.png"/></p>
                        <h2>Información general</h2>
                        <ul>
                            <li><b>Sucursal: </b><%= suc.getNombre()%></li>
                            <li><b>Teléfono: </b><%= suc.getTelefono()%></li>
                            <li><b>Fax: </b><%= suc.getFax()%></li>
                            <li><b>Direcci&oacute;n: </b><%= suc.getDir()%></li>
                            <% if (suc.isCentral()) {%>
                            <li><b>Sucursal Central: </b>Si</li>
                            <% } else {%>
                            <li><b>Sucursal Central: </b>No</li>
                            <% }%>
                        </ul>
                    </div>
                    <div class="gradient">
                        <h1>Alquileres de la sucursal</h1>
                        <% HashMap<String, Alquiler> alquileresSucursal = persistence.getAlquileres(null, null, suc.getCodSucursal(), null);
                                int[] alqMes = new int[12];
                                if (alquileresSucursal != null) {%>
                        <table>
                            <tr class="theader"><td>Marca</td><td>Modelo</td><td>Fechas</td><td>Importe</td><td></td></tr>
                            <% for (Alquiler alq : alquileresSucursal.values()) {
                                    alqMes[Tools.getMonthDate(alq.getFechaInicio()) - 1]++;%>
                            <tr>
                                <td><%= alq.getVehiculo().getMarca()%></td>
                                <td><%= alq.getVehiculo().getModelo()%></td>
                                <% if (alq.getFechaEntrega() != null) {%>
                                <td><%= Tools.printDate(alq.getFechaInicio())%> a <%= Tools.printDate(alq.getFechaEntrega())%></td>
                                <td><%= Tools.printBigDecimal(alq.getPrecio())%> €</td>
                                <% } else {%>
                                <td><%= Tools.printDate(alq.getFechaInicio())%> a <%= Tools.printDate(alq.getFechaFin())%></td>
                                <td>No finalizado</td>
                                <% }%>
                                <td><a title="Detalles Alquiler" href="/staf/viewrent.jsp?rent=<%= alq.getCodAlquiler() %>"><img alq="verAlq" src="/images/icons/viewRent.png"/></a></td>
                            </tr>
                            <% }%>
                        </table>
                        <p><b>Nota: </b>Los importes no tienen el IVA incluido</p>

                        <% } else {%>
                        <blockquote class="go">
                            <p>No se han encontrado alquileres para la sucursal actual</p>
                        </blockquote>
                        <% }%>
                    </div> 
                    <div class="gradient">
                        <h1>Incidencias de la sucursal</h1>
                        <% HashMap<String, Incidencia> incidenciasSuc = persistence.getIncidencias(null, null, suc.getCodSucursal());
                                    int[] incMes = new int[12];
                                    if (incidenciasSuc != null) {%>
                        <table>
                            <tr class="theader"><td>Observaciones</td><td>Fecha</td><td>Importe</td></tr>
                            <% for (Incidencia inc : incidenciasSuc.values()) {
                                    incMes[Tools.getMonthDate(inc.getFecha()) - 1]++;%>
                            <tr>
                                <td><b><%= inc.getTipoIncidencia().getNombre()%>: </b><%= inc.getObservaciones()%></td>
                                <td><%= Tools.printDate(inc.getFecha())%></td>
                                <td><%= Tools.printBigDecimal(inc.getPrecio())%> €</td>
                            </tr>
                            <% }%>
                        </table>
                        <p><b>Nota: </b>Los importes no tienen el IVA incluido</p>
                        <% } else {%>
                        <blockquote class="go">
                            <p>No se encontraron incidencias pertenecientes a este sucursal</p>
                        </blockquote>
                        <% }%>
                    </div>
                    <div class="gradient">
                        <h1>Registro de Facturación</h1>
                        <%
                            HashMap<String, Factura> facturasSucursal = persistence.getFacturas("1", "1", suc.getCodSucursal());
                            ArrayList<BigDecimal> facMes = new ArrayList<BigDecimal>(12);
                            for (int i = 0; i < 12; i++) {
                                facMes.add(BigDecimal.ZERO);
                            }
                            if (facturasSucursal != null) { %>
                            <table>
                                <tr class="theader"><td>Fecha</td><td>Importe</td><td>Importe + IVA</td><td>Estado</td><td>&nbsp;</td></tr>
                            <% for (Factura fac : facturasSucursal.values()) {
                                    facMes.set(Tools.getMonthDate(fac.getFechaEmision()) - 1, facMes.get(Tools.getMonthDate(fac.getFechaEmision()) - 1).add(fac.getImporteSinIVA()));%>
                            <tr>
                                <td><%= Tools.printDate(fac.getFechaEmision())%></td>
                                <td><%= Tools.printBigDecimal(fac.getImporteSinIVA())%> €</td>
                                <td><%= Tools.printBigDecimal(fac.getImporte())%> €</td>
                                <% if (!fac.isPagado()) {%>
                                <td>Sin Pagar</td>
                                <% } else {%>
                                <td>Pagada: <%= Tools.printDate(fac.getFechaPago())%></td>
                                <% }%>
                                <td><a title="Detalles Factura" href="/staf/viewbill.jsp?bill=<%= fac.getCodFactura()%>">
                                        <img src="/images/icons/bill.png" alt="VerFactura"/>
                                    </a></td>
                            </tr>
                            <% }%>
                        </table>
                        <p><b>NOTA: </b>El IVA aplicado en los precios es del <%= webConfig.getIVA()%> %</p>
                            <% } else { %>
                            <blockquote class="go">
                                <p>No se encontraron Facturas asignadas a esta sucursal</p>
                            </blockquote>
                            <% } %>
                    </div>
                    <div class="gradient">
                        <h1>Informes Gráficos</h1>
                        <h2>Grafíca de alquileres e incidencias mensuales por sucursal</h2>
                        <% if (alquileresSucursal != null || incidenciasSuc != null) {%>
                        <%@include file="/WEB-INF/include/chart_AlquileresIncidenciasSucursal.jsp" %>
                        <% } else {%>
                        <blockquote class="exclamation">
                            <p>Imposible generar la gráfica al no haber datos ni de alquileres ni de incidencias para el cliente</p>
                        </blockquote>
                        <% }%>
                        <br />
                        <h2>Grafíca de facturación mensual por sucursal</h2>
                        <% if (facturasSucursal != null) {%>
                        <%@include file="/WEB-INF/include/chart_FacturacionSucursal.jsp" %>
                        <% } else {%>
                        <blockquote class="exclamation">
                            <p>Imposible generar gráfica, no se han encontrado facturas para el cliente</p>
                        </blockquote>
                        <% }%>
                        <%@include file="/WEB-INF/include/chartRequired.jsp" %>
                        <p><b>NOTA: </b>La facturación indicada en la gráfica no incluye el IVA</p>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="stop">
                            <p>No se ha encontrado la sucursal seleccionada</p>
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


<%! private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("suc") != null) {
            try {
                Tools.validateUUID(request.getParameter("suc"));
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

