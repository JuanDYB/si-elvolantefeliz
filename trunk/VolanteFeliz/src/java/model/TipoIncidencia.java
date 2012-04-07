package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class TipoIncidencia {
    private String codTipoIncidencia;
    private String nombre;
    private String descripcion;
    private boolean abonaCliente;

    public TipoIncidencia(String codTipoIncidencia, String nombre, String descripcion, boolean abonaCliente) {
        this.codTipoIncidencia = codTipoIncidencia;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.abonaCliente = abonaCliente;
    }
    
    
}
