<%-- 
    Document   : manageclients
    Created on : 12-abr-2012, 11:35:09
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Cliente"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.Sucursal"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
    Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Gesti&oacute;n de Clientes</title>
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
                        <h1>Gestión de Clientes</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Operaciones disponibles</h2>
                        <ul>
                            <li><a href="/staf/newclient.jsp">Dar de alta nuevo cliente</a></li>
                            <% if (suc.isCentral()){ %>
                            <li><a href="/staf/manageclients.jsp?all=1">Mostrar todos los clientes</a></li>
                            <% } %>
                        </ul>
                    </div>
                    <!-- FIN BLOQUE GRADIENTE -->

                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient">
                        <h1>Clientes disponibles</h1>
                        <% HashMap<String, Cliente> clientes = null;
                        if (suc != null) {
                            if (request.getParameter("all") != null && request.getParameter("all").equals("1") && suc.isCentral()){
                                clientes = persistence.getClients(null, null);
                            }else{
                                clientes = persistence.getClients("codSucursal", suc.getCodSucursal());
                            }
                            if (clientes != null) {
                        %>                               
                        <p>
                            A continuación se muestra una tabla con los clientes pertenecientes a la sucursal
                            , dentro de la tabla podrá realizar las acciones que considere necesarias con un determinado cliente
                        </p>

                        <table border="0" align="center" width="90%" >
                            <tr><td>Nombre</td><td>DNI</td><td>Email</td><td>Empresa</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                            <% for (Cliente cli: clientes.values()){ %>
                            <tr>
                                <td><%= cli.getName() %></td>
                                <td><%= cli.getDni() %></td>
                                <td><%= cli.getEmail() %></td>
                                <% if (cli.getCompany() == null){ %>
                                <td>Cliente Particular</td>
                                <% } else{ %>
                                <td><%= cli.getCompany() %></td>
                                <% } %>
                                <td><a href="/staf/viewclient.jsp?cli=<%= cli.getCodCliente() %>"><img src="/images/icons/viewClient.png" alt="view" /></a></td>
                                <td><a href="/staf/editclient.jsp?cli=<%= cli.getCodCliente() %>"><img src="/images/icons/editClient.png" alt="edit" /></td>
                                <td><a href="/staf/delclient?cli=<%= cli.getCodCliente() %>"><img src="/images/icons/delete.png" alt="delete" /></td>
                            </tr>
                            <% } %>
                        </table>
                        <% } else {%>
                        <blockquote class="exclamation">
                            <p>
                                No se han encontrado clientes asignados a la sucursal actual
                            </p>
                        </blockquote>
                        <% }%>
                        <% } else {%>
                        <blockquote class="stop">
                            <p>
                                Ocurrio un error al obtener la sucursal actual a la que está asignado el empleado activo
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
