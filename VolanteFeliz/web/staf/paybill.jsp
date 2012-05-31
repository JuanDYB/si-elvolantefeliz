<%-- 
    Document   : paybill
    Created on : 14-may-2012, 12:53:26
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Factura"%>
<%@page import="model.Sucursal"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="model.Empleado"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (!validateEntry(request)) {
        response.sendError(404);
    }
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = (Sucursal) session.getAttribute("sucursal");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Pago Factura</title>
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


                    <% Factura bill = persistence.getFactura(request.getParameter("bill"), null);
                        if (bill != null && (bill.getCliente().getCodSucursal().equals(suc.getCodSucursal()) || suc.isCentral())) {%>
                    <% if (!bill.isPagado()) {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles de la factura</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Detalles generales</h2>
                        <ul>
                            <li><b>Codigo Factura: </b><%= bill.getCodFactura()%></li>
                            <li><b>Cliente: </b><a title="Ver Detalles del cliente" href="/staf/viewclient.jsp?cli=<%= bill.getCliente().getCodCliente()%>"><%= bill.getCliente().getName()%></a></li>
                            <% if (bill.getCliente().getCompany() == null) {%>
                            <li><b>Empresa: </b>Cliente Particular</li>
                            <% } else {%>
                            <li><b>Empresa: </b><%= bill.getCliente().getCompany()%></li>
                            <% }%>
                        </ul>
                        <h2>Detalles Económicos</h2>
                        <ul>
                            <li><b>Importe: </b><%= Tools.printBigDecimal(bill.getImporteSinIVA())%> €</li>
                            <li><b>IVA: </b><%= bill.getIVA()%> %</li>
                            <li><b>Importe con IVA: </b><%= Tools.printBigDecimal(bill.getImporte())%> €</li>
                            <li><b>Fecha Emisión: </b><%= Tools.printDate(bill.getFechaEmision())%></li>
                            <li><b>Pagado: </b>Factura pendiente de pago</li>
                        </ul>
                        <h2>Detalles de la factura completa</h2>
                        <img src="/images/icons/pdf.png" alt="pdf"/><a title="Ver factura completa" href="/staf/billFolder/<%= bill.getCodFactura()%>.pdf">Ver Factura completa en PDF</a>
                    </div>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Formulario de pago</h1>
                        <form title="payBill" method="POST" action="/staf/paybill">
                            <p><label>Forma de pago</label>
                            <select name="formaPago">
                                <option value="card">Tarjeta de crédito</option>
                                <option value="cash">Efectivo</option>
                                <option value="check">Talon bancario</option>
                            </select></p>
                            <input type="hidden" name="bill" value="<%= bill.getCodFactura() %>" />
                            <p><input name="pay" type="submit" value="Confirmar pago" /></p>
                        </form>
                    </div>
                    <% } else {%>
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Detalles de la factura</h1>
                        <blockquote class="exclamation">
                            <p>
                                No puede efectuar el pago de esta factura porque ya ha sido pagada previamente
                            </p>
                        </blockquote>
                    </div>
                    <% }%>
                <!-- FIN BLOQUE GRADIENTE -->
                <% } else if (bill != null && !bill.getCliente().getCodSucursal().equals(suc.getCodSucursal()) && !suc.isCentral()) {%>
                <!-- Gradiente color dentro de la columna principal -->
                <div class="gradient">
                    <blockquote class="exclamation">
                        <p>
                            No tiene permisos para mostrar esta factura porque no pertenece a su sucursal y tampoco es la sucursal central
                        </p>
                    </blockquote>
                </div>
                <% } else {%>
                <!-- Gradiente color dentro de la columna principal -->
                <div class="gradient">
                    <blockquote class="stop">
                        <p>
                            Ocurrio un error obteniendo la factura. Es posible que la factura solicitada no exista
                        </p>
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
    private boolean validateEntry(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("bill") != null) {
            try {
                Tools.validateUUID(request.getParameter("bill"));
            } catch (ValidationException ex) {
                return false;
            }
        }
        return true;
    }
%>

