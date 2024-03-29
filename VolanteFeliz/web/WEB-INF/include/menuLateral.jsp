<%-- 
    Document   : menuLateral
    Created on : 12-abr-2012, 9:12:27
    Author     : Juan D�ez-Yanguas Barber
--%>

<!-- Columna izquierda -->
<div class="width25 floatLeft leftColumn">
    <h1>Men�</h1>

    <ul class="sideMenu">
        <li class="here">
            General
            <ul>
                <li><a href="/index.jsp" title="Inicio">Inicio</a></li>
                <% if (session.getAttribute("login") != null && emplLogedIn != null ){ %>
                <li><a href="/logout" title="logout">Logout</a></li>
                <% } else{ %>
                <li><a href="/login.jsp" title="login">Login</a></li>
                <% } %>
                <li><a href="/about.jsp" title="about">Acerca de</a></li>
            </ul>
        </li>
    </ul>
    
    <% if (session.getAttribute("login") != null && emplLogedIn != null ){ %>
    <h1>Administraci&oacute;n</h1>
    <ul class="sideMenu">
        <% if(emplLogedIn.getPermisos() == 'e' || emplLogedIn.getPermisos() == 'a'){ %>
        <li class="here">
            Personal
            <ul>
                <li><a href="/staf/index.jsp" title="Inicio">Inicio</a></li>
                <li><a href="/staf/newclient.jsp" title="Nuevo cliente">Nuevo Cliente</a></li>
                <li><a href="/staf/manageclients.jsp" title="Gestion clientes">Gesti&oacute;n Clientes</a></li>
                <li><a href="/staf/bill_management.jsp" title="Gestion facturacion">Gesti&oacute;n Facturaci&oacute;n</a></li>
                <li><a href="/staf/newrent.jsp?st=1" title="Nuevo alquiler">Nuevo Alquiler</a></li>
                <li><a href="/staf/manage_rent.jsp" title="Gestionar alquileres">Gesti�n Alquileres</a></li>
                <li><a href="/staf/newincidence.jsp" title="Nueva incidencia">Nueva Incidencia</a></li>
                <li><a href="/staf/manage_incidence.jsp" title="Gesti�n incidencias">Gesti�n Incidencias</a></li>
                <li><a href="/staf/manage_vehicles.jsp" title="Gesti�n incidencias">Gesti�n Veh�culos</a></li>
            </ul>
        </li>
        <% if(emplLogedIn.getPermisos() == 'a'){ %>
        <li class="here">
            Administraci&oacute;n
            <ul>
                <li><a href="/staf/administration/index.jsp" title="Inicio">Inicio</a></li>
                <li><a href="/staf/administration/viewsuc.jsp?suc=<%= emplLogedIn.getCodSucursal() %>" title="Detalles Sucursal" >Informaci�n Sucursal</a></li>
                <li><a href="/staf/administration/manage_suc.jsp" title="Gesti�n sucursales">Gesti�n Sucursales</a></li>
                <li><a href="/staf/administration/new_employee.jsp" title="Nuevo empleado">Nuevo Empleado</a></li>
                <li><a href="/staf/administration/manage_empl.jsp" title="Gestion empleados">Gesti�n de empleados</a></li>
                <li><a href="/staf/administration/new_vehicle.jsp" title="Nuevo veh�culo">Nuevo veh�culo</a></li>
            </ul>
        </li>
        <% } %>
        <% } %>
    </ul>
    
    <% } %>

</div>
<!-- FIN COLUMNA IZQUIERDA -->
