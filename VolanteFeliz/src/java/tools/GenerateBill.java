package tools;

import java.math.BigDecimal;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import model.Alquiler;
import model.Incidencia;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class GenerateBill {
    String [] alquileres;
    HashMap <String, Alquiler> alquileresFacturar;
    String [] incidencias;
    HashMap <String, Incidencia> incidenciasFacturar;
    HttpServletRequest request;
    BigDecimal precioFinal = BigDecimal.ZERO;

    public GenerateBill(String[] alquileres, HashMap<String, Alquiler> alquileresFacturar, String[] incidencias, HashMap<String, Incidencia> incidenciasFacturar, HttpServletRequest request) {
        this.alquileres = alquileres;
        this.alquileresFacturar = alquileresFacturar;
        this.incidencias = incidencias;
        this.incidenciasFacturar = incidenciasFacturar;
        this.request = request;
    }
    
    private void doLoopIncidences (){
        for (int i = 0; i < incidencias.length; i++){
            Incidencia inc = incidenciasFacturar.get(incidencias[i]);
            if (inc == null){
                request.setAttribute("resultados", "Componentes de factura eliminados");
                Tools.anadirMensaje(request, "Se ha eliminado la incidencia: " + incidencias[i] + " por resultar ya facturada", 'w');
            }else if (inc.getTipoIncidencia().isAbonaCliente()){
                precioFinal = precioFinal.add(inc.getPrecio());
            }
        }
    }
    
    private void doLoopAlquileres (){
        for (int i = 0; i < alquileres.length; i++){
            Alquiler alq = alquileresFacturar.get(alquileres[i]);
            if (alq == null){
                request.setAttribute("resultados", "Componentes de factura eliminados");
                Tools.anadirMensaje(request, "Se ha eliminado el alquiler: " + alquileres[i] + " por resultar ya facturado", 'w');
            }else{
                
            }
        }
    }
    
    private BigDecimal calculateRentPrice (Alquiler alq){
        return null;
    }
    
}
