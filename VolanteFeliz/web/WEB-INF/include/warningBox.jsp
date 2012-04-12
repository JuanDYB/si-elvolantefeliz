<%-- 
    Document   : warningBox
    Created on : 12-abr-2012, 1:48:48
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="java.util.ArrayList"%>
<%-- Solo se muestra si hay resultados de una operaci&oacute;n que mostrar --%>
<% if (request.getAttribute("resultados") != null) {
        ArrayList<String> listado = (ArrayList<String>) request.getAttribute("listaResultados");
        if (((Character) request.getAttribute("severityResultados")) == 'w') {%>
<blockquote class="exclamation">
    <% } else if (((Character) request.getAttribute("severityResultados")) == 'e') {%>
<blockquote class="stop">
    <% } else {%>
<blockquote class="go">
    <% }%>
    <h2><%= request.getAttribute("resultados")%></h2>
    <ul>
        <% for (String actual : listado) {%>
        <li><%= actual%></li>
        <% }%>
    </ul>
</blockquote>
<% }%>


