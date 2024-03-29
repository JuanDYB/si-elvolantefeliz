<%-- 
    Document   : chart_FacturacionCliente
    Created on : 20-may-2012, 0:02:30
    Author     : Juan Díez-Yanguas Barber
--%>

<%-- METER TODO EN CONTENIDO HTML --%>
<script type="text/javascript">
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'chart_FacturacionCliente',
                type: 'column'
            },
            title: {
                text: 'Estad�sticas mensuales por cliente'
            },
            subtitle: {
                text: 'Estad�sticas mensuales de facturaci�n por cliente (sin IVA)'
            },
            xAxis: {
                categories: [
                    'Ene',
                    'Feb',
                    'Mar',
                    'Abr',
                    'May',
                    'Jun',
                    'Jul',
                    'Ago',
                    'Sep',
                    'Oct',
                    'Nov',
                    'Dic'
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Importe sin IVA (\u20AC)'
                }
            },
            legend: {
                layout: 'vertical',
                backgroundColor: '#FFFFFF',
                align: 'left',
                verticalAlign: 'top',
                x: 100,
                y: 70,
                floating: true,
                shadow: true
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +': '+ this.y + ' \u20AC';
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
                series: [{
                name: 'Facturaci�n (\u20AC)',
                data: [<%= Tools.printBigDecimal(facMes.get(0)) %>, <%= Tools.printBigDecimal(facMes.get(1)) %>, 
<%= Tools.printBigDecimal(facMes.get(2)) %>, <%= Tools.printBigDecimal(facMes.get(3)) %>, <%= Tools.printBigDecimal(facMes.get(4)) %>, 
<%= Tools.printBigDecimal(facMes.get(5)) %>, <%= Tools.printBigDecimal(facMes.get(6)) %>, <%= Tools.printBigDecimal(facMes.get(7)) %>, 
<%= Tools.printBigDecimal(facMes.get(8)) %>, <%= Tools.printBigDecimal(facMes.get(9)) %>, <%= Tools.printBigDecimal(facMes.get(10)) %>, 
<%= Tools.printBigDecimal(facMes.get(11)) %>]
    
            }]
        });
    });
    
});
</script>

<div id="chart_FacturacionCliente" style="min-width: 400px; height: 400px; margin: 0 auto"></div> 
