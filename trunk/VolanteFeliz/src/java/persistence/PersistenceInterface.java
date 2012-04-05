package persistence;

import java.util.HashMap;
import model.Cliente;
import model.Empleado;
import model.Sucursal;

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
    
    public HashMap <String, Cliente> getClients ();
    
    public HashMap <String, Empleado> getEmpleados ();
    
    public boolean editClient (String codCliente, Cliente client);
    
    public boolean deleteClient (String codCliente);
}
