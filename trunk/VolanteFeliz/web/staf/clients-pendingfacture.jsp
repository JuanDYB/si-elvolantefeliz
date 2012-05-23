<%-- 
    Document   : clients_pendingfacture
    Created on : 09-abr-2012, 10:22:33
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="model.Cliente"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>

<!DOCTYPE html>

<%
boolean central = false;
if (!this.validateEntry(request)){
    response.sendError(404);
}else{
    if (request.getParameter("central").equals("1")){
        central = true;
    }
}
Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());

%>

<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Clientes para facturar</title>
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
                        <h1>Clientes con elementos pendientes de facturar</h1>
                        <% if (suc.isCentral()){ %>
                        <ul>
                            <li><a href="/staf/clients-pendingfacture.jsp?central=0">Mostrar clientes con facturacion pendiente para esta sucursal</a></li>
                            <li><a href="/staf/clients-pendingfacture.jsp?central=1">Mostrar clientes con facturacion pendiente para todas las sucursales</a></li>
                        </ul>
                        <% } %>
                        <% 
                            HashMap<String, Cliente> clientes1;
                            HashMap<String, Cliente> clientes2;
                            if (central){
                                clientes1 = persistence.getClientsToFactureRent(null);
                                clientes2 = persistence.getClientsToFactureIncidence(null);
                            }else{
                                clientes1 = persistence.getClientsToFactureRent(((Empleado) session.getAttribute("empleado")).getCodSucursal());
                                clientes2 = persistence.getClientsToFactureIncidence(((Empleado) session.getAttribute("empleado")).getCodSucursal());
                            }
                            
                            String type = "alq";
                            if (clientes1 != null && clientes2 != null) {
                                clientes1.putAll(clientes2);
                                type = "all";
                            } else if (clientes2 != null && clientes1 == null) {
                                clientes1 = clientes2;
                                type = "inc";
                            }
                            if (clientes1 == null) {%>
                        <p>
                            No se han encontrado clientes en esta sucursal con alquileres pendientes de facturar
                        </p>
                        <% } else {%>
                        <table>
                            <tr class="theader"><td>Nombre</td><td>DNI</td><td>Empresa</td><td>Telefono</td><td>&nbsp;</td></tr>
                            <% for (Cliente cli : clientes1.values()) {%>
                            <tr>
                                <td><%= cli.getName()%></td>
                                <td><%= cli.getDni()%></td>
                                <% if (cli.getCompany() == null){ %>
                                <td>Cliente particular</td>
                                <% }else{ %>
                                <td><%= cli.getCompany()%></td>
                                <% } %>
                                <td><%= cli.getTelephone()%></td>
                                <td><a href="/staf/client-facturepending.jsp?cli=<%= cli.getCodCliente()%>&type=<%= type%>"><img src="/images/icons/selecCli.png" alt="selecCli" /></a></td>
                            </tr>
                            <% }%>
                        </table>
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

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"here\""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>

<%!

    private boolean validateEntry (HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("central") != null){
            if (request.getParameter("central").equals("1") || request.getParameter("central").equals("0")){
                return true;
            }
        }
        return false;
    }
 
%>

