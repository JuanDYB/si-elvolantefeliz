<%-- 
    Document   : pend-rentingfactureclient
    Created on : 09-abr-2012, 11:49:48
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Incidencia"%>
<%@page import="model.Alquiler"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Cliente"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="tools.Tools"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% if (validateEntry(request) == false) {
        response.sendError(404);
        return;
    }
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Cliente cliente = persistence.getClient(request.getParameter("cli"));
    String type = request.getParameter("type");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alquileres pendientes de facturar</title>
    </head>
    <body>
        <% if (cliente != null) {
            boolean empresa = false;
            if(cliente.getCompany() != null) empresa = true;
        %>
        <form name="elegirAlquileresFactura" action="" >
            <% if (type.equals("all") || type.equals("alq")){ %>
        <h1>Alquileres pendientes de facturar del cliente: <%= cliente.getName()%></h1>
        <% HashMap<String, Alquiler> alqSinFacturarCliente = persistence.getAlquileresClienteSinFacturar(cliente);
            if (alqSinFacturarCliente != null) { %>
            <table border="0" align="center" width="90%">
                <% if (!empresa) { %>
                <select>
                <% } %>
                <tr><td>&nbsp;</td><td>Fecha Salida</td><td>Fecha Entrada</td><td>Matricula</td><td>Marca</td><td>Importe</td></tr>
                <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                <% for (Alquiler alq: alqSinFacturarCliente.values()){ %>
                <tr>
                    <% if (empresa) { %>
                    <td><input type="checkbox" name="alquiler" value="<%= alq.getCodAlquiler() %>" /></td>
                    <% } else{ %>
                    <td><option value="<%= alq.getCodAlquiler() %>"></option></td>
                    <% } %>
                    <td><%= alq.getFechaInicio() %></td>
                    <td><%= alq.getFechaEntrega() %></td>
                    <td><%= alq.getVehiculo().getMatricula() %></td>
                    <td><%= alq.getVehiculo().getMarca() %></td>
                    <td><%= Tools.printBigDecimal(alq.getPrecio()) %></td>
                </tr>
                <% } %>
            </table>
            <% if (!empresa){ %>
            </select>
            <% }
                           }
            if (type.equals("all") || type.equals("inc")){ 
                HashMap<String, Incidencia> incSinFacturarCliente = persistence.getIncidenciasClienteSinFacturar(cliente);
                if (incSinFacturarCliente != null){ %>
            <h1>Incidencias pendientes de facturar</h1>
            <table border="0" align="center" width="90%">
                <tr><td>&nbsp;</td><td>Tipo Incidencia</td><td>Fecha</td><td>Precio</td></tr>
                <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                <% for (Incidencia inc: incSinFacturarCliente.values()){ %>
                <tr>
                    <td><input type="checkbox" name="incidencia" value="<%= inc.getCodIncidencia() %>" /></td>
                    <td><%= inc.getTipoIncidencia().getNombre() %></td>
                    <td><%= inc.getFecha() %></td>
                    <td><%= Tools.printBigDecimal(inc.getPrecio()) %></td>
                </tr>
                <% } %>
            </table>
            
            <% } %>
            <input name="genFact" type="submit" value="Generar Factura" />
            </form>
            <% } else {%>
            <p>Ha ocurrido un error obteniendo los datos de los alquileres del cliente pendientes de facturar</p>
            <% }
        } else {%>
        <h1>Cliente no encontrado</h1>
        <p>El cliente seleccionado para ver los alquileres pendientes de facturar no se ha encontrado</p>
        <% }%>
    </body>
</html>

<%! private boolean validateEntry(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 2 && request.getParameter("cli") != null && request.getParameter("type") != null) {
            if (request.getParameter("type").equals("all") || request.getParameter("type").equals("alq") 
                    || request.getParameter("type").equals("inc")){
                return Tools.validateUUID(request.getParameter("cli"));
            }
        }
        return false;
    }%>
