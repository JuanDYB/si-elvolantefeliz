<%-- 
    Document   : pending_paybill
    Created on : 
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Cliente"%>
<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Factura"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page import="tools.Tools"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (request.getParameter("all") != null && request.getParameter("cli") != null) {
        response.sendError(404);
        return;
    }
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
    boolean continuar = false;
    Cliente cli = null;
    HashMap<String, Factura> facturasSinPagar = null;
    if (request.getParameter("cli") != null) {
        try {
            Tools.validateUUID(request.getParameter("cli"));
            cli = persistence.getClient(request.getParameter("cli"));
        } catch (ValidationException ex) {
            response.sendError(404);
            return;
        }
    }
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Zona de empleados</title>
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


                    <% if (suc != null && request.getParameter("cli") != null && cli != null && (cli.getCodSucursal().equals(suc.getCodSucursal()) || suc.isCentral())) {
                            facturasSinPagar = persistence.getFacturasPendientesPago(cli.getCodCliente(), null);
                            continuar = true;%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Información del cliente</h1>
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
                    <% } else if (suc != null && request.getParameter("cli") != null && cli != null && !cli.getCodSucursal().equals(suc.getCodSucursal()) && !suc.isCentral()) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>No tiene permisos para consultar este cliente, no pertenece a su sucursal y la sucursal no es central</p>
                        </blockquote>
                    </div>
                    <% } else if (suc != null && request.getParameter("cli") != null) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <blockquote class="stop">
                            <p>No se encontro el cliente seleccionado para buscar las facturas pendientes de pago</p>
                        </blockquote>
                    </div>
                    <% } else if (suc != null && request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()) {
                        facturasSinPagar = persistence.getFacturasPendientesPago(null, null);
                        continuar = true;
                    } else if (suc != null && request.getParameter("all") == null) {
                        facturasSinPagar = persistence.getFacturasPendientesPago(null, suc.getCodSucursal());
                        continuar = true;
                    } else if (suc != null && request.getParameter("all").equals("1") && !suc.isCentral()) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <blockquote class="exclamation">
                            <p>La sucursal actual no es central, solo puede ver las facturas pendientes de pago de su susursal</p>
                        </blockquote>
                    </div>
                    <% } else {%>
                    <div class="gradient">
                        <blockquote class="stop">
                            <p>La sucursal de la que desea obtener las facturas pendientes de pago no se ha encontrado</p>
                        </blockquote>
                    </div>
                    <% }%>

                    <% if (continuar && facturasSinPagar != null) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Facturas pendientes de pago</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>

                        <table>

                            <tr class="theader"><td>Cliente</td><td>Fecha</td><td>Importe</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <% for (Factura fact : facturasSinPagar.values()) {%>
                            <tr>
                                <td><%= fact.getCliente().getName()%></td>
                                <td><%= Tools.printDate(fact.getFechaEmision())%></td>
                                <td><%= Tools.printBigDecimal(fact.getImporteSinIVA())%> €</td>
                                <td><a title="Detalles Factura" href="/staf/viewbill.jsp?bill=<%= fact.getCodFactura()%>">
                                        <img src="/images/icons/bill.png" alt="VerFactura"/></a>
                                </td>
                                <td><a title="Pagar Factura" href="/staf/paybill.jsp?bill=<%= fact.getCodFactura()%>">
                                        <img src="/images/icons/pay.png" alt="pagarFactura" /></a>
                                </td>      
                            </tr>
                            <% }%>
                        </table>
                        <p><b>NOTA: </b>Los importes se especifican sin el IVA incluido</p>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->
                    <% } else if (continuar && facturasSinPagar == null) {%>
                    <div class="gradient">
                        <h1>Facturas pendientes de pago</h1>
                        <blockquote class="exclamation">
                            <p>No se han encontrado facturas pendientes de pago</p>
                        </blockquote>
                    </div>
                    <% } else {%>

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
