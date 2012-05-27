<%-- 
    Document   : chart_AlquileresIncidenciasSucursal
    Created on : 27-may-2012, 2:09:01
    Author     : Juan D�ez-Yanguas Barber
--%>

<script type="text/javascript">
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'chart_AlquileresIncidenciasSucursal',
                type: 'column'
            },
            title: {
                text: 'Estad�sticas mensuales por sucursal'
            },
            subtitle: {
                text: 'Estad�sticas mensuales por n�mero de alquileres e incidencias por sucursal'
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
                    text: 'Cantidad'
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
                        this.x +': '+ this.y;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
                series: [{
                name: 'Alquileres',
                data: [<%= alqMes[0] %>, <%= alqMes[1] %>, <%= alqMes[2] %>, <%= alqMes[3] %>, <%= alqMes[4] %>, <%= alqMes[5] %>, 
<%= alqMes[6] %>, <%= alqMes[7] %>, <%= alqMes[8] %>, <%= alqMes[9] %>, <%= alqMes[10] %>, <%= alqMes[11] %>]
    
            }, {
                name: 'Incidencias',
                data: [<%= incMes[0] %>, <%= incMes[1] %>, <%= incMes[2] %>, <%= incMes[3] %>, <%= incMes[4] %>, <%= incMes[5] %>, 
                    <%= incMes[6] %>, <%= incMes[7] %>, <%= incMes[8] %>, <%= incMes[9] %>, <%= incMes[10] %>, <%= incMes[11] %>]
    
            }]
        });
    });
    
});
</script>

<div id="chart_AlquileresIncidenciasSucursal" style="min-width: 400px; height: 400px; margin: 0 auto"></div> 

