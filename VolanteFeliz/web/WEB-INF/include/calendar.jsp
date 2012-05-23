<%-- 
    Document   : calendar
    Created on : 20-may-2012, 19:26:23
    Author     : JuanDYB
--%>
<script type="text/javascript">
/* Inicialización en español para la extensión 'UI date picker' para jQuery. */
/* Traducido por Vester (xvester@gmail.com). */
jQuery(function($){
	$.datepicker.regional['es'] = {
		closeText: 'Cerrar',
		prevText: '&#x3c;Ant',
		nextText: 'Sig&#x3e;',
		currentText: 'Hoy',
		monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
		'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
		monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun',
		'Jul','Ago','Sep','Oct','Nov','Dic'],
		dayNames: ['Domingo','Lunes','Martes','Mi&eacute;rcoles','Jueves','Viernes','S&aacute;bado'],
		dayNamesShort: ['Dom','Lun','Mar','Mi&eacute;','Juv','Vie','S&aacute;b'],
		dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','S&aacute;'],
		weekHeader: 'Sm',
		dateFormat: 'dd/mm/yy',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
	$.datepicker.setDefaults($.datepicker.regional['es']);
});
$(document).ready(function() {
           $("#fechainicio, #fechafin").datepicker({
               numberOfMonths: 1,
               autoSize: true,
               showAnim: 'show',
               changeMonth: true,
               showOn: 'button',
               buttonImageOnly: true,
               buttonImage: '/images/icons/datepicker.png',
               buttonText: "Seleccione la fecha",
               dateFormat: 'dd-mm-yy'
           });
        });
    </script>
    
    
