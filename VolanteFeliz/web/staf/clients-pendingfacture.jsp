<%-- 
    Document   : clients_rentingfacture
    Created on : 09-abr-2012, 10:22:33
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Empleado"%>
<%@page import="model.Cliente"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Clientes para facturar</title>
    </head>
    <body>
        <h1>Clientes con alquileres sin facturar</h1>
        <% PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
        HashMap <String, Cliente> clientes1 = persistence.getClientsToFactureRent(((Empleado)session.getAttribute("empleado")).getCodSucursal());
        HashMap <String, Cliente> clientes2 = persistence.getClientsToFactureIncidence(((Empleado)session.getAttribute("empleado")).getCodSucursal());
        String type = "alq";
        if (clientes1 != null && clientes2 != null){
            clientes1.putAll(clientes2);
            type = "all";
        }else if (clientes2 != null && clientes1 == null){
            clientes1 = clientes2;
            type = "inc";
        }
        if (clientes1 == null){ %>
        <p>
            No se han encontrado clientes en esta sucursal con alquileres pendientes de facturar
        </p>
        <% }else{ %>
        <table border="0" align="center" width="90%">
            <tr><td>Nombre</td><td>DNI</td><td>Empresa</td><td>Telefono</td><td>Telefono</td><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
            <% for (Cliente cli: clientes1.values()){ %>
            <tr>
                <td><%= cli.getName() %></td>
                <td><%= cli.getDni() %></td>
                <td><%= cli.getCompany() %></td>
                <td><%= cli.getTelephone() %></td>
                <td><a href="/staf/client-facturepending.jsp?cli=<%= cli.getCodCliente() %>&type=<%= type %>"><img src="/images/icons/selecCli.png" alt="selecCli" /></a></td>
            </tr>
            <% } %>
        </table>
        
         
    </body>
</html>
