<%-- 
    Document   : index
    Created on : 23-mar-2012, 10:08:22
    Author     : Juan Díez-Yanguas Barber
--%>

<%@page import="model.Empleado"%>
<%@page import="persistence.PersistenceInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Empleado emplLogedIn = (Empleado) session.getAttribute("empleado");
%>
<html>
    <head>
        <%@include file="/WEB-INF/include/HTML_Header.jsp" %>
        <title>Inicio</title>
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
                        <h1>¡Bienvenido a El Volante Feliz!</h1>
                        <h3>
                            <img alt="icono" src="/images/logoGrande.png" class="floatLeft" /><br /><br />
                            Le damos la bienvenida a El Volante Feliz, donde los vehículos están felices y los clientes también lo están
                            <br /><br />
                            Somos la empresa líder en el sector del alquiler de vehículos con un alto grado de 
                            satisfacción por parte de los clientes, quienes aseguran estar totalmente satisfechos con los servicios 
                            prestados y la atención recibida por parte de nuestro personal<br /><br />
                        </h3>
                        <br />
                    </div>
                    
                    <!-- Gradiente color dentro de la columna principal -->
                    <div class="gradient clear">
                        <h1>Gran Variedad de Vehículos</h1>
                        <p style="text-align: center"><img width="97%" height="50%" class="floatLeft" src="/images/GamaMediaBaja.png" alt="gama media" /></p>
                        <div class="width50 floatRight">
                            <h4>Marcas con las que trabajamos</h4>
                            <ul>
                                <li>Renault</li>
                                <li>Toyota</li>
                                <li>Mercedes</li>
                                <li>BMW</li>
                                <li>Lexus</li>
                                <li>Audi</li>
                                <li>Volkswagen</li>
                                <li>Porsche</li>
                            </ul>
                            <p>Y muchas más...</p>
                        </div>
                        <div class="width50 floatLeft">
                            <h4>Tipos de vehículos</h4>
                            <ul>
                                <li>Vehículos mixtos</li>
                                <li>Monovolúmenes</li>
                                <li>Berlinas</li>
                                <li>Todo Terrenos</li>
                                <li>Deportivos</li>
                            </ul>
                        
                            <h4>Gamas disponibles</h4>
                            <ul>
                                <li>Gama baja</li>
                                <li>Gama media</li>
                                <li>Gama alta</li>
                            </ul>
                        </div>
                        <p style="text-align: center"><img width="97%" height="50%" class="floatLeft" src="/images/GamaAlta.png" alt="gama alta" /></p>
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

<%! String menuInicio = "class=\"here\""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = ""; %>
<%! String menuContacto = ""; %>
