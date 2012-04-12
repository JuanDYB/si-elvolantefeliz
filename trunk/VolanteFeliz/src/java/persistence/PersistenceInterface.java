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
    
    public Sucursal getSucursal (String codSucursal);
    
    public HashMap <String, Incidencia> getIncidenciasAlquiler(String codAlquiler);
    
    public HashMap <String, Cliente> getClients (String campo, String valor);
    
    public HashMap <String, Cliente> getClientsToFactureRent (String codSucursal);
    
    public HashMap <String, Cliente> getClientsToFactureIncidence (String codSucursal);
    
    public HashMap <String, Alquiler> getAlquileresClienteSinFacturar (Cliente cli);
    
    public HashMap <String, Incidencia> getIncidenciasClienteSinFacturar (Cliente cli);
    
    public HashMap <String, Empleado> getEmpleados ();
    
    public HashMap <String, Alquiler> getAlquileres (String campo, String valor);
    
    //Ha de ser una operación unitaria para evitar problemas de concurrencia (otro empleado factura 
    public Factura generarFactura (String [] alquileres, String [] incidencias);
    
    public boolean editClient (String codCliente, Cliente client);
    
    public boolean deleteClient (String codCliente); //Terminar...
}