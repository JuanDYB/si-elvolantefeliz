package persistence;

import java.util.HashMap;
import model.*;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public interface PersistenceInterface {
    
    public boolean init (String recurso, String nameBD);
    
    public boolean exit ();
    
    public int numAdmin ();
    
    public int numCentrales ();
    
    public boolean addClient (Cliente client);
    
    public boolean addEmpleado (Empleado empl);
    
    public Boolean addSucursal (Sucursal suc);
    
    public Cliente getClient (String codCliente);
    
    public Empleado getEmployee (String campo, String valor);
    
    public TipoIncidencia getTipoInciencia (String codTipoIncidencia);
    
    public Incidencia getIncidencia (String campo, String valor);
    
    public Vehiculo getVehiculo (String campo, String valor);
    
    public Tarifa getTarifa (String codTarifa);
    
    public HashMap <String, Incidencia> getIncidenciasAlquiler(String codAlquiler);
    
    public HashMap <String, Cliente> getClients ();
    
    public HashMap <String, Empleado> getEmpleados ();
    
    public HashMap <String, Alquiler> getAlquileres (String campo, String valor);
    
    public boolean editClient (String codCliente, Cliente client);
    
    public boolean deleteClient (String codCliente); //Terminar...
}
