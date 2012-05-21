<%-- 
    Document   : manage_rent
    Created on : 22-may-2012, 0:37:34
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Factura"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
%>

<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Gestión de Facturas</title>
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
                        <h1>Gestión de Alquileres</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Acciones disponibles</h2>
                        <ul>
                            
                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->

                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Facturas disponibles</h1>

                        
                        <table>
                            <tr class="theader"><td>Cliente</td><td>Fecha</td><td>Importe</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <% for (Factura fact : facturas.values()) {%>
                            <tr>
                                <td><%= fact.getCliente().getName()%></td>
                                <td><%= Tools.printDate(fact.getFechaEmision())%></td>
                                <td><%= Tools.printBigDecimal(fact.getImporteSinIVA())%> €</td>
                                <td><a title="Detalles Factura" href="/staf/viewbill.jsp?bill=<%= fact.getCodFactura()%>">
                                        <img src="/images/icons/bill.png" alt="VerFactura"/>
                                    </a></td>
                                    <% if (fact.isPagado()) {%>
                                <td><img src="/images/icons/facPagada.png" alt="facPagada" title="Factura Pagada" /></td>
                                    <% } else {%>
                                <td><a title="Pagar Factura" href="/staf/paybill.jsp?bill=<%= fact.getCodFactura()%>">
                                        <img src="/images/icons/pay.png" alt="pagarFactura" />
                                    </a></td>
                                    <% }%>
                            </tr>
                            <% }%>
                        </table>
                        <p><b>NOTA: </b>Los importes se especifican sin el IVA incluido</p>
                        <% } else {%>
                        <blockquote class="exclamation" >
                            <p>
                                No se han encontrado facturas en el sistema
                            </p>
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

