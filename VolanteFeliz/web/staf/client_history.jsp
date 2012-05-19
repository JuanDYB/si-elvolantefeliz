<%-- 
    Document   : client_history
    Created on : 19-may-2012, 19:15:12
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="java.math.BigDecimal"%>
<%@page import="model.Factura"%>
<%@page import="tools.WebConfig"%>
<%@page import="model.Incidencia"%>
<%@page import="model.Alquiler"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Cliente"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateForm(request)) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Cliente cli = persistence.getClient(request.getParameter("cli"));
    Sucursal suc = persistence.getSucursal(cli.getCodSucursal());
    WebConfig webConfig = (WebConfig) application.getAttribute("appConfig");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <title>Historial de cliente</title>
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


                    <% if (suc.getCodSucursal().equals(cli.getCodSucursal()) || suc.isCentral()) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles Cliente</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Informaci&oacute;n General del Cliente</h2>
                        <ul>

                            <li><b>Nombre: </b><%= cli.getName()%></li>
                            <li><b>Email: </b><%= cli.getEmail()%></li>
                            <li><b>DNI: </b><%= cli.getDni()%></li>
                            <li><b>Dirección: </b><%= cli.getAddress()%></li>
                            <li><b>Tel&eacute;fono: </b><%= cli.getTelephone()%></li>
                            <% if (cli.getCompany() == null) {%>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% } else {%>
                            <li><b>Empresa: </b><%= cli.getCompany()%></li>
                            <% }%>
                            <li><b>Edad: </b><%= cli.getAge()%></li>

                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Historial de Alquileres</h1>
                        <%
                            HashMap <String, Alquiler> alquileresCliente = persistence.getAlquileres("codCliente", cli.getCodCliente());
                            int [] alqMes = new int [12];
                            if (alquileresCliente != null){ %>
                        <table>
                            <tr class="theader"><td>Marca</td><td>Modelo</td><td>Fechas</td><td>Importe</td></tr>
                            <% for (Alquiler alq: alquileresCliente.values()){ 
                                alqMes[Tools.getMonthDate(alq.getFechaInicio()) - 1] ++; %>
                            <tr>
                                <td><%= alq.getVehiculo().getMarca() %></td>
                                <td><%= alq.getVehiculo().getModelo() %></td>
                                <td><%= Tools.printDate(alq.getFechaInicio()) %> a <%= Tools.printDate(alq.getFechaEntrega()) %></td>
                                <td><%= Tools.printBigDecimal(alq.getPrecio()) %> €</td>
                            </tr>
                            <% } %>
                        </table>
                        <p><b>Nota: </b>Los importes no tienen el IVA incluido</p>
                        <% } else{ %>
                        <blockquote class="exclamation">
                            <p>No se han encontrado alquileres para el cliente actual</p>
                        </blockquote>
                        <% } %>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Historial de Incidencias</h1>
                        <%
                            HashMap <String, Incidencia> incidenciasCliente = persistence.getIncidencias ("codCliente", cli.getCodCliente());
                            int [] incMes = new int [12];
                            if (alquileresCliente != null){ %>
                        <table>
                            <tr class="theader"><td>Observaciones</td><td>Fecha</td><td>Importe</td></tr>
                            <% for (Incidencia inc: incidenciasCliente.values()){ 
                                incMes[Tools.getMonthDate(inc.getFecha()) - 1] ++; %>
                            <tr>
                                <td><b><%= inc.getTipoIncidencia().getNombre() %>: </b><%= inc.getObservaciones() %></td>
                                <td><%= Tools.printDate(inc.getFecha()) %></td>
                                <td><%= Tools.printBigDecimal(inc.getPrecio()) %> €</td>
                            </tr>
                            <% } %>
                        </table>
                        <p><b>Nota: </b>Los importes no tienen el IVA incluido</p>
                        <% } else{ %>
                        <blockquote class="exclamation">
                            <p>No se han encontrado incidencias para el cliente actual</p>
                        </blockquote>
                        <% } %>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Historial de Facturación</h1>
                        <%
                            HashMap <String, Factura> facturasCliente = persistence.getFacturas("codCliente", cli.getCodCliente(), null);
                            ArrayList <BigDecimal> facMes = new ArrayList <BigDecimal>(12);
                            for (int i = 0; i < 12; i++) facMes.add(BigDecimal.ZERO);
                            if (facturasCliente != null){
                        %>
                        <table>
                            <tr class="theader"><td>Fecha</td><td>Importe</td><td>Importe + IVA</td><td>Estado</td></tr>
                            <% for (Factura fac: facturasCliente.values()){ 
                                facMes.set(Tools.getMonthDate(fac.getFechaEmision()) - 1, facMes.get(Tools.getMonthDate(fac.getFechaEmision()) - 1).add(fac.getImporteSinIVA())); %>
                            <tr>
                                <td><%= Tools.printDate(fac.getFechaEmision()) %></td>
                                <td><%= Tools.printBigDecimal(fac.getImporteSinIVA()) %> €</td>
                                <td><%= Tools.printBigDecimal(fac.getImporte()) %> €</td>
                                <% if(!fac.isPagado()){ %>
                                <td>Sin Pagar</td>
                                <% } else{ %>
                                <td>Pagada: <%= Tools.printDate(fac.getFechaPago()) %></td>
                                <% } %>
                            </tr>
                            <% } %>
                        </table>
                        <p><b>NOTA: </b>El IVA aplicado en los precios es: <%= webConfig.getIVA() %> %</p>
                        <% } else{ %>
                        <blockquote class="exclamation">
                            <p>No se han encontrado facturas para el cliente actual</p>
                        </blockquote>
                        <% } %>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Informes Gráficos</h1>
                        <%@include file="/WEB-INF/include/chart_AlquileresIncidenciasCliente.jsp" %>
                        <br />
                        <%@include file="/WEB-INF/include/chart_FacturacionCliente.jsp" %>
                        <%@include file="/WEB-INF/include/chartRequired.jsp" %>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    
                    <% } else if (cli != null && !suc.getCodSucursal().equals(cli.getCodSucursal()) && !suc.isCentral()){%>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No tiene permisos de ver los derechos de este cliente porque no pertenece a esta sucursal
                            </p>
                        </blockquote>
                    </div>
                    <% }else{ %>
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>
                                No se ha encontrado el cliente seleccionado
                            </p>
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


<%! private boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("cli") != null) {
            try {
                Tools.validateUUID(request.getParameter("cli"));
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

