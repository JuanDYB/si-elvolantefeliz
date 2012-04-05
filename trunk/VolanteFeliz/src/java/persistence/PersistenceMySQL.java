package persistence;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import model.Cliente;
import model.Empleado;
import model.Sucursal;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class PersistenceMySQL implements PersistenceInterface {

    private static final PersistenceMySQL instance = new PersistenceMySQL();
    private DataSource pool;
    private String nameBD;
    private static final Logger logger = Logger.getLogger(PersistenceMySQL.class.getName());

    private PersistenceMySQL() {
    }

    public static PersistenceMySQL getInstance() {
        return instance;
    }

    @Override
    public boolean init(String recurso, String nameBD) {
        this.nameBD = nameBD;
        try{
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            pool = (DataSource) env.lookup(recurso);
            if (pool == null) {
                logger.log(Level.SEVERE, "DataSource no encontrado");
                return false;
            }
            return true;
        }catch(NamingException ex){
            logger.log(Level.SEVERE, "Ha ocurrido un error buscando el recurso de BD. No es posible iniciar", ex);
            return false;
        }
    }

    @Override
    public boolean exit() {
        return true;
    }

    @Override
    public boolean addClient(Cliente client) {
        Connection conexion = null;
        PreparedStatement insert = null;
        boolean ok = false;
        try{
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Cliente "
                    + "VALUES (?,?,?,?,?,?,?,?,?)");
            insert.setString(1, client.getCodCliente());
            insert.setString(2, client.getName());
            insert.setString(3, client.getEmail());
            insert.setString(4, client.getDni());
            insert.setString(5, client.getAddress());
            insert.setString(6, client.getTelephone());
            insert.setString(7, client.getCompany());
            insert.setString(8, client.getCodSucursal());
            insert.setInt(9, client.getAge());
            if (insert.executeUpdate() == 1){
                ok = true;
            }
            
        }catch (SQLException ex){
            logger.log(Level.SEVERE, "Error insertando un cliente en BD", ex);
        }finally{
            cerrarConexionesYStatementsm(conexion, insert);
        }
        return ok;
    }

    @Override
    public Cliente getClient(String codCliente) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Cliente client = null;
        try{
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Cliente "
                    + "WHERE codCliente=?");
            select.setString(1, codCliente);
            rs = select.executeQuery();
            while (rs.next()){
                client = new Cliente (codCliente, rs.getString("Nombre"), rs.getString("Email"),
                        rs.getString("DNI"), rs.getString("Direccion"), rs.getString("Telefono"),
                        rs.getString("Empresa"), rs.getString("codSucursal"), rs.getInt("Edad"));
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE, "Ha ocurrido un error obteniendo el cliente de la BD", ex);
        }finally{
            cerrarResultSets(rs);
            cerrarConexionesYStatementsm(conexion, select);
        }
        return client;
    }

    @Override
    public boolean editClient(String codCliente, Cliente client) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteClient(String codCliente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Empleado getEmployee(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Empleado empl = null;
        try{
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Empleado WHERE ?=?");
            select.setString(1, campo);
            select.setString(2, valor);
            rs = select.executeQuery();
            while (rs.next()){
                empl = new Empleado (rs.getString("codEmpleado"), rs.getString("UserName"), rs.getString("Pass")
                        , rs.getString("Nombre"), rs.getString("DNI"), rs.getString("Telefono")
                        , rs.getString("Direccion"), rs.getString("codSucursal"), rs.getString("Permisos").charAt(0));
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE, "Error obteniendo un empleado de la base de datos", ex);
            empl = null;
        }finally{
            cerrarResultSets(rs);
            cerrarConexionesYStatementsm(conexion, select);
        }
        return empl;
    }

    @Override
    public int numAdmin() {
        Connection conexion = null;
        PreparedStatement count = null;
        ResultSet rs = null;
        int numAdmin = -1;
        try{
            conexion = pool.getConnection();
            count = conexion.prepareStatement("SELECT COUNT(Permisos) AS num FROM " + nameBD + ".Empleado WHERE Permisos = 'a'");
            rs = count.executeQuery();
            while (rs.next()) {
                numAdmin = rs.getInt("num");
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE, "Error obteniendo numero de administradores", ex);
        }finally{
            cerrarResultSets(rs);
            cerrarConexionesYStatementsm(conexion, count);
        }
        return numAdmin;
    }
    
    @Override
    public int numCentrales() {
        Connection conexion = null;
        PreparedStatement count = null;
        ResultSet rs = null;
        int numAdmin = -1;
        try{
            conexion = pool.getConnection();
            count = conexion.prepareStatement("SELECT COUNT(Central) AS num FROM " + nameBD + ".Empleado WHERE Central=true");
            rs = count.executeQuery();
            while (rs.next()) {
                numAdmin = rs.getInt("num");
            }
        }catch (SQLException ex){
            logger.log(Level.SEVERE, "Error obteniendo numero de administradores", ex);
        }finally{
            cerrarResultSets(rs);
            cerrarConexionesYStatementsm(conexion, count);
        }
        return numAdmin;
    }

    @Override
    public HashMap<String, Cliente> getClients() {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Clientes");
            rs = select.executeQuery();
            while (rs != null) {
                //Cliente cl = new Cliente();
                //clientes.put(cl.getCodCliente(), cl);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error en la obtencion de clientes", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatementsm(conexion, select);
        }
        if (clientes.isEmpty()) {
            return null;
        }
        return clientes;
    }

    @Override
    public HashMap<String, Empleado> getEmpleados() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean addEmpleado(Empleado empl) {
        Connection conexion = null;
        PreparedStatement insert = null;
        boolean ok = false;
        try{
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Empleado VALUES (?,?,?,?,?,?,?,?)");
            insert.setString(1, empl.getCodEmpleado());
            insert.setString(2, empl.getUserName());
            insert.setString(3, empl.getName());
            insert.setString(4, empl.getDni());
            insert.setString(5, empl.getTelephone());
            insert.setString(6, empl.getAddress());
            insert.setString(7, empl.getCodSucursal());
            insert.setObject(8, empl.getPermisos(), java.sql.Types.CHAR, 1);
            if (insert.executeUpdate() == 1){
                ok = true;
            }
        }catch (SQLException ex){
            logger.log (Level.SEVERE, "Ha ocurrido un error insertando un empleado", ex);
        }finally{
            cerrarConexionesYStatementsm(conexion, insert);
        }
        return ok;
    }

    @Override
    public Boolean addSucursal(Sucursal suc) {
        Connection conexion = null;
        PreparedStatement insert = null;
        Boolean ok = false;
        if (suc.isCentral() && this.numCentrales() >= 1){
            logger.log(Level.SEVERE, "No se puede añadir la sucursal porque ha sido marcada como central y ya existe una");
            return null;
        }
        try{
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Sucursal VALUES (?,?,?,?,?,?)");
            insert.setString(1, suc.getCodSucursal());
            insert.setString(2, suc.getNombre());
            insert.setString(3, suc.getDir());
            insert.setString(4, suc.getTelefono());
            insert.setString(5, suc.getFax());
            insert.setBoolean(6, suc.isCentral());
            if (insert.executeUpdate() == 1){
                ok = true;
            }
        }catch (SQLException ex){
            logger.log (Level.SEVERE, "Ha ocurrido un error insertando un empleado", ex);
        }finally{
            cerrarConexionesYStatementsm(conexion, insert);
        }
        return ok;
    }

    private void cerrarConexionesYStatementsm(Connection conexion, Statement... st) {
        for (Statement statement : st) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error al cerrar un statement", ex);
                }
            }
        }
        try {
            conexion.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al cerrar una conexion", ex);
        }
    }

    private void cerrarResultSets(ResultSet... result) {
        for (ResultSet rs : result) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error al cerrar ResultSet", ex);
                }
            }
        }
    }
}
