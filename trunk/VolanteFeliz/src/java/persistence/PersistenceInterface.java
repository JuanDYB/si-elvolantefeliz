package persistence;

import java.util.HashMap;
import model.Cliente;
import model.Empleado;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public interface PersistenceInterface {
    
    public boolean init (String recurso, String nameBD);
    
    public boolean exit ();
    
    public boolean addClient (Cliente client);
    
    public Cliente getClient (String codCliente);
    
    public boolean editClient (String codCliente, Cliente client);
    
    public boolean deleteClient (String codCliente);
    
    public HashMap <String, Cliente> getClients ();
    
    public boolean addEmpleado (Empleado empl);
    
    public Integer numAdmin ();
    
    public HashMap <String, Empleado> getEmpleados ();
    
    public Empleado getEmployee (String codEmpleado);
}
