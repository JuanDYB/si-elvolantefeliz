<%-- 
    Document   : editclient.jsp
    Created on : 16-abr-2012, 9:46:19
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Sucursal"%>
<%@page import="org.owasp.esapi.errors.ValidationException"%>
<%@page import="tools.Tools"%>
<%@page import="model.Cliente"%>
<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
PersistenceInterface persistence = (PersistenceInterface) application.getAttribute("persistence");
Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
Cliente cli = persistence.getClient(request.getParameter("cli"));
Sucursal suc = persistence.getSucursal(emplLogedIn.getCodSucursal());
if (!this.validateForm(request)){
    response.sendError(404);
    return;
}
%>
<html>
    <head>
        <script type="text/javascript" src="/scripts/jquery-1.7.2.js"></script>
        <script type="text/javascript" src="/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/validacion.css" media="screen, tv, projection" />
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
        <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
        <title>Editar Cliente</title>
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
                        <h1>Gesti&oacute;n de Clientes</h1>
                        <%@include file="/WEB-INF/include/warningBox.jsp" %>
                        <h2>Editar cliente</h2>
                        <% if (cli != null && (cli.getCodSucursal().equals(suc.getCodSucursal()) || suc.isCentral())){ %>
                        <p>
                            Tiene a su disposición el siguiente formulario para editar el cliente seleccionado
                        </p>
                        <form name="editclient" method="POST" action="/staf/editclient">
                            <p>
                                <label>Nombre</label>
                                <input value="<%= cli.getName() %>" name="name" type="text" size="70" maxlength="200" class=":name :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Direcci&oacute;n</label>
                                <label>Ejemplo: Calle, 1 28002-Madrid</label>
                                <input value="<%= cli.getAddress() %>" name="address" type="text" size="70" maxlength="400" class=":dir :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Teléfono</label>
                                <input value="<%= cli.getTelephone() %>" name="phone" type="text" size="20" maxlength="14" class=":tlf :required :only_on_blur" />
                            </p>
                            <p>
                                <label>Empresa</label>
                                <% if (cli.getCompany() == null){ %>
                                <input name="company" type="text" size="70" maxlength="100" class=":name :only_on_blur" />
                                <% }else{ %>
                                <input value="<%= cli.getCompany() %>" name="company" type="text" size="70" maxlength="100" class=":name :only_on_blur" />
                                <% } %>
                            </p>
                            <p>
                                <label>Email</label>
                                <input value="<%= cli.getEmail() %>" name="email" type="text" size="70" maxlength="100" class=":email :required :only_on:blur"/>
                            </p>
                            <p>
                                <label>Edad</label>
                                <input value="<%= cli.getAge() %>" name="age" type="text" size="5" maxlength="3" class=":age :required :only_on_blur"/>
                            </p>
                            <p>
                                <input type="hidden" value="<%= cli.getCodCliente() %>" name="codCliente" />
                                <input name="send" type="submit" value="Confirmar Edici&oacute;n" />
                            </p>
                        </form>
                        <% } else if (cli != null && !emplLogedIn.getCodSucursal().equals(cli.getCodSucursal())){ %>
                        <blockquote class="exclamation" >
                            <p>
                                No puede editar el cliente seleccionado porque no pertenece a esta sucursal
                            </p>
                        </blockquote>
                        <% } else{ %>
                        <blockquote class="exclamation" >
                            <p>
                                No se puede editar el cliente seleccionado porque no se ha encontrado dado de alta en el sistema
                            </p>
                        </blockquote>
                        <% } %>
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

<%!
private boolean validateForm (HttpServletRequest request){
    if (request.getParameterMap().size() >= 1  && request.getParameter("cli") != null){
        try{
            Tools.validateUUID (request.getParameter("cli"));
            return true;
        } catch (ValidationException ex){
            return false;
        }
    }
    return false;
}
%>

<%! String menuInicio = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"here\""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
