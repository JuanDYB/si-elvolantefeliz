<%-- 
    Document   : menu
    Created on : 12-abr-2012, 1:10:27
    Author     : Juan Díez-Yanguas Barber
--%>

<!-- Menu horizontal. Activo class=here -->
<div id="mainMenu">
    <ul class="floatRight">
        <li><a href="/index.jsp" title="Inicio" <%= menuInicio %> >Inicio</a></li>
        <% if (session.getAttribute("login") != null && emplLogedIn != null && emplLogedIn.getPermisos() == 'e'){ %>
        <li><a href="/staf/index.jsp" title="staf" <%= menuPreferencias %> >Personal</a></li>
        <% } else if (emplLogedIn != null && emplLogedIn.getPermisos() == 'a'){ %>
        <li><a href="/staf/administration/index.jsp" title="administration" <%= menuPreferencias %> >Administraci&oacute;n</a></li>
        <% } else{ %>
        <li><a href="/login.jsp" title="Login" <%= menuLogin %> >Login</a></li>
        <% } %>
        <li><a href="/about.jsp" title="Acerca de" <%= menuAbout %> >Acerca de</a></li>
        <li><a href="/contact.jsp" title="Contacta" <%= menuContacto %> >Contacto</a></li>
    </ul>
</div>
<!-- FIN MENU HORIZONTAL -->
