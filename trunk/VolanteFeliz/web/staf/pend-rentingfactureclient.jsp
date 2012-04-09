<%-- 
    Document   : pend-rentingfactureclient
    Created on : 09-abr-2012, 11:49:48
    Author     : Juan Díez-Yanguas Barber
--%>

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
        <h1>Alquileres pendientes de facturar del cliente: <%= cliente.getName()%></h1>
        <% HashMap<String, Alquiler> alqSinFacturarCliente = persistence.getAlquileresClienteSinFacturar(cliente);
            if (alqSinFacturarCliente != null) {%>
            <form name="elegirAlquileresFactura" action="" >
            <% if (!empresa) { %>
            <select>
            <% } %>
            <table border="0" align="center" width="90%">
                <tr><td>&nbsp;</td><td>Fecha Salida</td><td>Fecha Entrada</td><td>Matricula</td><td>Marca</td><td>Importe</td></tr>
                <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                <% for (Alquiler alq: alqSinFacturarCliente.values()){ %>
                <tr>
                    <% if (empresa) { %>
                    <td><input type="checkbox" name="alquileres" value="<%= alq.getCodAlquiler() %>" /></td>
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
            <% } %>
            <input name="genFact" type="submit" value="Generar Factura" />
            </form>
            <% } else {%>
            <p>Ha ocurrido un error obteniendo los datos de los alquileres del cliente pendientes de facturar</p>
            <% }
        } else {%>
        <h1>Cliente no encontrada</h1>
        <p>El cliente seleccionado para ver los alquileres pendientes de facturar no se ha encontrado</p>
        <% }%>
    </body>
</html>

<%! private boolean validateEntry(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("cli") != null) {
            return Tools.validateUUID(request.getParameter("cli"));
        }
        return false;
    }%>
