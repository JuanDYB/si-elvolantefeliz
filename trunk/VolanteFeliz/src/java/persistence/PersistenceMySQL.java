package persistence;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import model.*;
import tools.CalculateRentPrice;
import tools.GenerateBill;
import tools.GeneratePDFBill;
import tools.Tools;

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

    public void setDebug(MysqlDataSource pool) {
        this.pool = pool;
    }

    @Override
    public boolean init(String recurso, String nameBD) {
        this.nameBD = nameBD;
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            pool = (DataSource) env.lookup(recurso);
            if (pool == null) {
                logger.log(Level.SEVERE, "DataSource no encontrado");
                return false;
            }
            return true;
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "Ha ocurrido un error buscando el recurso de BD. No es posible iniciar", ex);
            return false;
        }
    }

    @Override
    public boolean exit() {
        return true;
    }

    @Override
    public int numAdmin() {
        Connection conexion = null;
        PreparedStatement count = null;
        ResultSet rs = null;
        int numAdmin = -1;
        try {
            conexion = pool.getConnection();
            count = conexion.prepareStatement("SELECT COUNT(Permisos) AS num FROM " + nameBD + ".Empleado WHERE Permisos = 'a'");
            rs = count.executeQuery();
            while (rs.next()) {
                numAdmin = rs.getInt("num");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo numero de administradores", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, count);
        }
        return numAdmin;
    }

    @Override
    public int numCentrales() {
        Connection conexion = null;
        PreparedStatement count = null;
        ResultSet rs = null;
        int numAdmin = -1;
        try {
            conexion = pool.getConnection();
            count = conexion.prepareStatement("SELECT COUNT(Central) AS num FROM " + nameBD + ".Empleado WHERE Central=true");
            rs = count.executeQuery();
            while (rs.next()) {
                numAdmin = rs.getInt("num");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo numero de administradores", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, count);
        }
        return numAdmin;
    }

    @Override
    public boolean addClient(Cliente client) {
        Connection conexion = null;
        PreparedStatement insert = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Cliente "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)");
            insert.setString(1, client.getCodCliente());
            insert.setString(2, client.getDni());
            insert.setString(3, client.getName());
            insert.setInt(4, client.getAge());
            insert.setString(5, client.getCompany());
            insert.setString(6, client.getAddress());
            insert.setString(7, client.getTelephone());
            insert.setString(8, client.getEmail());
            insert.setString(9, client.getCodSucursal());
            insert.setBoolean(10, true);
            if (insert.executeUpdate() == 1) {
                ok = true;
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error insertando un cliente en BD", ex);
        } finally {
            cerrarConexionesYStatement(conexion, insert);
        }
        return ok;
    }

    @Override
    public boolean addEmpleado(Empleado empl) {
        Connection conexion = null;
        PreparedStatement insert = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Empleado VALUES (?,?,?,?,?,?,?,?,?)");
            insert.setString(1, empl.getCodEmpleado());
            insert.setString(2, empl.getName());
            insert.setString(3, empl.getUserName());
            insert.setString(4, empl.getPass());
            insert.setString(5, empl.getDni());
            insert.setString(6, empl.getTelephone());
            insert.setString(7, empl.getAddress());
            insert.setString(8, empl.getCodSucursal());
            insert.setObject(9, empl.getPermisos(), java.sql.Types.CHAR, 1);
            if (insert.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ha ocurrido un error insertando un empleado", ex);
        } finally {
            cerrarConexionesYStatement(conexion, insert);
        }
        return ok;
    }

    @Override
    public Boolean addSucursal(Sucursal suc) {
        Connection conexion = null;
        PreparedStatement insert = null;
        Boolean ok = false;
        if (suc.isCentral() && this.numCentrales() >= 1) {
            logger.log(Level.SEVERE, "No se puede añadir la sucursal porque ha sido marcada como central y ya existe una");
            return null;
        }
        try {
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Sucursal VALUES (?,?,?,?,?,?)");
            insert.setString(1, suc.getCodSucursal());
            insert.setString(2, suc.getNombre());
            insert.setString(3, suc.getDir());
            insert.setString(4, suc.getTelefono());
            insert.setString(5, suc.getFax());
            insert.setBoolean(6, suc.isCentral());
            if (insert.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ha ocurrido un error insertando un empleado", ex);
        } finally {
            cerrarConexionesYStatement(conexion, insert);
        }
        return ok;
    }

    @Override
    public Cliente getClient(String codCliente) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Cliente client = null;
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Cliente "
                    + "WHERE codCliente=?");
            select.setString(1, codCliente);
            rs = select.executeQuery();
            while (rs.next()) {
                client = new Cliente(codCliente, rs.getString("Nombre"), rs.getString("Email"),
                        rs.getString("DNI"), rs.getString("Direccion"), rs.getString("Telefono"),
                        rs.getString("Empresa"), rs.getString("codSucursal"), rs.getInt("Edad"), rs.getBoolean("Activo"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ha ocurrido un error obteniendo el cliente de la BD", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        return client;
    }

    @Override
    public Empleado getEmployee(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Empleado empl = null;
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Empleado WHERE " + campo + "=?");
            select.setString(1, valor);
            rs = select.executeQuery();
            while (rs.next()) {
                empl = new Empleado(rs.getString("codEmpleado"), rs.getString("Nombre"), rs.getString("UserName"), rs.getString("Pass"), rs.getString("DNI"), rs.getString("Telefono"), rs.getString("Direccion"), rs.getString("codSucursal"), rs.getString("Permisos").charAt(0));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo un empleado de la base de datos", ex);
            empl = null;
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        return empl;
    }

    @Override
    public Incidencia getIncidencia(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Incidencia incidencia = null;
        TipoIncidencia tipoIncidencia = null;
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Incidencia WHERE " + campo + "=?");
            select.setString(1, valor);
            rs = select.executeQuery();
            while (rs.next()) {
                tipoIncidencia = this.getTipoInciencia(rs.getString("codTipoIncidencia"), null);
                if (tipoIncidencia != null) {
                    incidencia = new Incidencia(rs.getString("codIncidencia"), tipoIncidencia, rs.getString("codAlquiler"), rs.getString("codCliente"), rs.getDate("Fecha"), rs.getString("Observaciones"), rs.getBigDecimal("Precio"));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo una incidencia de la base de datos", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        return incidencia;
    }

    @Override
    public Vehiculo getVehiculo(String campo, String valor, Connection conExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Vehiculo vehicle = null;
        try {
            if (conExterna != null) {
                conexion = conExterna;
            } else {
                conexion = pool.getConnection();
            }
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Vehiculo WHERE " + campo + "=?");
            select.setString(1, valor);
            rs = select.executeQuery();
            while (rs.next()) {
                vehicle = new Vehiculo(rs.getString("codVehiculo"), rs.getString("Matricula"), rs.getString("Marca"), rs.getString("Modelo"), rs.getString("nBastidor"), rs.getInt("CapCombustible"), rs.getString("codSucursal"), rs.getString("codType"), rs.getString("codRevision"), rs.getString("codITV"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo un vehiculo de la base de datos", ex);
        } finally {
            if (conExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        return vehicle;
    }

    @Override
    public Tarifa getTarifa(String codTarifa, Connection conExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Tarifa tarifa = null;
        try {
            if (conExterna != null) {
                conexion = conExterna;
            } else {
                conexion = pool.getConnection();
            }
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Tarifa WHERE codTarifa=?");
            select.setString(1, codTarifa);
            rs = select.executeQuery();
            while (rs.next()) {
                tarifa = new Tarifa(rs.getString("codTarifa"), rs.getString("Nombre"), rs.getString("Descripcion"), rs.getBigDecimal("PrecioBase"), rs.getBigDecimal("PrecioDia"), rs.getBigDecimal("PrecioDiaExtra"), rs.getBigDecimal("PrecioCombustible"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo un vehiculo de la base de datos", ex);
        } finally {
            if (conExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        return tarifa;
    }

    @Override
    public Sucursal getSucursal(String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Sucursal suc = null;
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Sucursal WHERE codSucursal=?");
            select.setString(1, codSucursal);
            rs = select.executeQuery();
            while (rs.next()) {
                suc = new Sucursal(codSucursal, rs.getString("Nombre"), rs.getString("Direccion"), rs.getString("Telefono"), rs.getString("Fax"), rs.getBoolean("Central"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo los datos de una sucursal");
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        return suc;
    }

    @Override
    public Alquiler getAlquiler(String codAlquiler, Connection conExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Alquiler alq = null;
        try {
            if (conExterna == null) {
                conexion = pool.getConnection();
            } else {
                conexion = conExterna;
            }
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Alquiler WHERE codAlquiler=?");
            select.setString(1, codAlquiler);
            rs = select.executeQuery();
            while (rs.next()) {
                Cliente cli = this.getClient(rs.getString("codCliente"));
                Vehiculo vehicle = this.getVehiculo("codVehiculo", rs.getString("codVehiculo"), null);
                Tarifa tarifa = this.getTarifa(rs.getString("codTarifa"), null);
                if (cli != null && vehicle != null && tarifa != null) {
                    alq = new Alquiler(codAlquiler, cli, vehicle, tarifa, rs.getDate("FechaInicio"), rs.getDate("FechaFin"), rs.getDate("FechaEntrega"), rs.getBigDecimal("Importe"), rs.getInt("KMInicio"), rs.getInt("KMFin"), rs.getInt("CombustibleFin"), rs.getString("Observaciones"));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo un alquiler de la Base de Datos", rs);
        } finally {
            if (conExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        return alq;
    }

    @Override
    public Factura getFactura(String codFactura, Connection conexionExterna) {
        Connection conexion = null;
        PreparedStatement selectFactura = null;
        PreparedStatement selectElementoFactura = null;
        ResultSet rsFactura = null;
        ResultSet rsElementoFactura = null;
        Factura factura = null;
        try {
            if (conexionExterna == null) {
                conexion = pool.getConnection();
            } else {
                conexion = conexionExterna;
            }
            selectFactura = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Factura WHERE codFactura=?");
            selectFactura.setString(1, codFactura);
            rsFactura = selectFactura.executeQuery();
            while (rsFactura.next()) {
                HashMap<String, Alquiler> alquileresFactura = new HashMap<String, Alquiler>();
                HashMap<String, Incidencia> incidenciasFactura = new HashMap<String, Incidencia>();
                selectElementoFactura = conexion.prepareStatement("SELECT codAlquiler FROM " + nameBD + ".AlquilerFactura "
                        + "WHERE codFactura=?");
                selectElementoFactura.setString(1, codFactura);
                rsElementoFactura = selectElementoFactura.executeQuery();
                while (rsElementoFactura.next()) {
                    Alquiler alq = this.getAlquiler(rsElementoFactura.getString("codAlquiler"), null);
                    if (alq != null) {
                        alquileresFactura.put(alq.getCodAlquiler(), alq);
                    }
                }
                selectElementoFactura = conexion.prepareStatement("SELECT codIncidencia FROM " + nameBD + ".IncidenciaFactura "
                        + "WHERE codFactura =?");
                selectElementoFactura.setString(1, codFactura);
                rsElementoFactura = selectElementoFactura.executeQuery();
                while (rsElementoFactura.next()) {
                    Incidencia inc = this.getIncidencia("codIncidencia", rsElementoFactura.getString("codIncidencia"));
                    if (inc != null) {
                        incidenciasFactura.put(inc.getCodIncidencia(), inc);
                    }
                }
                Cliente cli = this.getClient(rsFactura.getString("codCliente"));
                if (cli != null) {
                    if (incidenciasFactura.isEmpty()) {
                        incidenciasFactura = null;
                    }
                    if (alquileresFactura.isEmpty()) {
                        alquileresFactura = null;
                    }
                    factura = new Factura(codFactura, cli, alquileresFactura, incidenciasFactura, rsFactura.getInt("IVA"), rsFactura.getBigDecimal("ImporteSinIVA"), rsFactura.getBigDecimal("Importe"), rsFactura.getDate("FechaEmision"), rsFactura.getString("FormaPago"), rsFactura.getDate("FechaPago"), rsFactura.getBoolean("Pagado"));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo una factura de la Base de Datos", ex);
        } finally {
            cerrarResultSets(rsFactura, rsElementoFactura);
            if (conexionExterna == null) {
                cerrarConexionesYStatement(conexion, selectFactura, selectElementoFactura);
            }
        }
        return factura;
    }

    @Override
    public HashMap<String, Incidencia> getIncidencias(String campo, String valor, String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Incidencia> incidencias = new HashMap<String, Incidencia>();
        try {
            conexion = pool.getConnection();
            if (campo != null && valor != null){
                select = conexion.prepareStatement("SELECT codIncidencia FROM " + nameBD + ".Incidencia WHERE " + campo + "=?");
            select.setString(1, valor);
            }else{
                select = conexion.prepareStatement("SELECT codIncidencia "
                        + "FROM " + nameBD + ".Incidencia inc, " + nameBD + ".Cliente cli "
                        + "WHERE cli.codCliente=? AND inc.codCliente=cli.codCliente");
                select.setString(1, codSucursal);
            }
            rs = select.executeQuery();
            incidencias = new HashMap<String, Incidencia>();
            while (rs.next()) {
                String codIncidencia = rs.getString("codIncidencia");
                Incidencia incidencia = this.getIncidencia("codIncidencia", codIncidencia);
                if (incidencia != null) {
                    incidencias.put(codIncidencia, incidencia);
                } else {
                    incidencias.clear();
                    break;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo incidencias", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (incidencias.isEmpty()) {
            return null;
        }
        return incidencias;
    }

    @Override
    public TipoIncidencia getTipoInciencia(String codTipoIncidencia, Connection conExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        TipoIncidencia tipoIncidencia = null;
        try {
            if (conExterna != null) {
                conexion = conExterna;
            } else {
                conexion = pool.getConnection();
            }
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".TipoIncidencia WHERE codTipoIncidencia=?");
            select.setString(1, codTipoIncidencia);
            rs = select.executeQuery();
            while (rs.next()) {
                tipoIncidencia = new TipoIncidencia(codTipoIncidencia, rs.getString("Nombre"), rs.getString("Descripcion"), rs.getBoolean("AbonaCliente"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo el tipo de incidencia: " + codTipoIncidencia, ex);
        } finally {
            if (conExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        return tipoIncidencia;
    }

    @Override
    public HashMap<String, Cliente> getClients(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
        try {
            conexion = pool.getConnection();
            if (campo != null && valor != null) {
                select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Cliente WHERE " + campo + "=?");
                select.setString(1, valor);
            } else {
                select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".Cliente");
            }
            rs = select.executeQuery();
            clientes = new HashMap<String, Cliente>();
            while (rs.next()) {
                Cliente cl = new Cliente(rs.getString("codCliente"), rs.getString("Nombre"), rs.getString("Email"), rs.getString("DNI"), rs.getString("Direccion"), rs.getString("Telefono"), rs.getString("Empresa"), rs.getString("codSucursal"), rs.getInt("Edad"), rs.getBoolean("Activo"));
                clientes.put(cl.getCodCliente(), cl);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error en la obtencion de clientes", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (clientes.isEmpty()) {
            return null;
        }
        return clientes;
    }

    /*
     * SELECT AlqNotFact.codAlquiler, alq.codCliente FROM (SELECT codAlquiler
     * FROM Alquiler WHERE codAlquiler NOT IN (SELECT codAlquiler FROM
     * AlquilerFactura)) AlqNotFact, Alquiler alq, Cliente cli where
     * alq.codAlquiler = AlqNotFact.codAlquiler AND alq.FechaEntrega IS NOT NULL
     * AND cli.codCliente=alq.codCliente;
     */
    @Override
    public HashMap<String, Cliente> getClientsToFactureRent(String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
        try {
            conexion = pool.getConnection();
            if (codSucursal != null) {
                select = conexion.prepareStatement("SELECT cli.codCliente, cli.DNI, cli.Nombre, cli.Edad, cli.Empresa, cli.Direccion"
                        + ", cli.Telefono, cli.Email, cli.Activo "
                        + "FROM (SELECT codAlquiler FROM " + nameBD + ".Alquiler WHERE codAlquiler "
                        + "NOT IN (SELECT codAlquiler FROM " + nameBD + ".AlquilerFactura)) AlqNotFact, "
                        + nameBD + ".Cliente cli, " + nameBD + ".Alquiler alq "
                        + "WHERE cli.codSucursal=? AND cli.codCliente=alq.codCliente AND alq.FechaEntrega IS NOT NULL "
                        + "AND alq.codAlquiler=AlqNotFact.codAlquiler");
                select.setString(1, codSucursal);
            } else {
                select = conexion.prepareStatement("SELECT cli.codCliente, cli.DNI, cli.Nombre, cli.Edad, cli.Empresa, cli.Direccion"
                        + ", cli.Telefono, cli.Email, cli.Activo "
                        + "FROM (SELECT codAlquiler FROM " + nameBD + ".Alquiler WHERE codAlquiler "
                        + "NOT IN (SELECT codAlquiler FROM " + nameBD + ".AlquilerFactura)) AlqNotFact, "
                        + nameBD + ".Cliente cli, " + nameBD + ".Alquiler alq "
                        + "WHERE cli.codCliente=alq.codCliente AND alq.FechaEntrega IS NOT NULL "
                        + "AND alq.codAlquiler=AlqNotFact.codAlquiler");
            }
            rs = select.executeQuery();
            while (rs.next()) {
                String codCliente = rs.getString("cli.codCliente");
                Cliente cli = new Cliente(codCliente, rs.getString("cli.Nombre"), rs.getString("cli.Email"), rs.getString("cli.DNI"), rs.getString("cli.Direccion"), rs.getString("cli.Telefono"), rs.getString("cli.Empresa"), codSucursal, rs.getInt("cli.Edad"), rs.getBoolean("cli.Activo"));
                clientes.put(codCliente, cli);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo clientes con alquileres terminados pendientes de facturar", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (clientes.isEmpty()) {
            return null;
        }
        return clientes;
    }

    /*
     * SELECT IncNotFact.codincidencia, alq.codCliente FROM (SELECT
     * codIncidencia FROM Incidencia WHERE codIncidencia NOT IN (SELECT
     * codIncidencia FROM IncidenciaFactura)) IncNotFact, Incidencia inc,
     * Cliente cli where inc.codIncidencia = IncNotFact.codincidencia AND
     * cli.codCliente=alq.codCliente;
     */
    @Override
    public HashMap<String, Cliente> getClientsToFactureIncidence(String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
        try {
            conexion = pool.getConnection();
            if (codSucursal != null) {
                select = conexion.prepareStatement("SELECT cli.codCliente, cli.DNI, cli.Nombre, cli.Edad, cli.Empresa, cli.Direccion"
                        + ", cli.Telefono, cli.Email, cli.Activo "
                        + "FROM (SELECT codIncidencia FROM " + nameBD + ".Incidencia WHERE codIncidencia "
                        + "NOT IN (SELECT codIncidencia FROM " + nameBD + ".IncidenciaFactura)) IncNotFact, "
                        + nameBD + ".Cliente cli, " + nameBD + ".Incidencia inc "
                        + "WHERE cli.codSucursal=? AND cli.codCliente=inc.codCliente AND IncNotFact.codIncidencia=inc.codIncidencia");
                select.setString(1, codSucursal);
            } else {
                select = conexion.prepareStatement("SELECT cli.codCliente, cli.DNI, cli.Nombre, cli.Edad, cli.Empresa, cli.Direccion"
                        + ", cli.Telefono, cli.Email, cli.Activo "
                        + "FROM (SELECT codIncidencia FROM " + nameBD + ".Incidencia WHERE codIncidencia "
                        + "NOT IN (SELECT codIncidencia FROM " + nameBD + ".IncidenciaFactura)) IncNotFact, "
                        + nameBD + ".Cliente cli, " + nameBD + ".Incidencia inc "
                        + "WHERE cli.codCliente=inc.codCliente AND IncNotFact.codIncidencia=inc.codIncidencia");
            }
            rs = select.executeQuery();
            while (rs.next()) {
                String codCliente = rs.getString("cli.codCliente");
                Cliente cli = new Cliente(codCliente, rs.getString("cli.Nombre"), rs.getString("cli.Email"), rs.getString("cli.DNI"), rs.getString("cli.Direccion"), rs.getString("cli.Telefono"), rs.getString("cli.Empresa"), codSucursal, rs.getInt("cli.Edad"), rs.getBoolean("cli.Activo"));
                clientes.put(codCliente, cli);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo clientes con alquileres terminados pendientes de facturar", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (clientes.isEmpty()) {
            return null;
        }
        return clientes;
    }

    @Override
    public HashMap<String, Alquiler> getAlquileresClienteSinFacturar(Cliente cli) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Alquiler> alquileresCliente = new HashMap<String, Alquiler>();
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT alq.codAlquiler, alq.codCliente, alq.codVehiculo, alq.codTarifa, alq.FechaInicio, "
                    + "alq.FechaFin, alq.FechaEntrega, alq.Importe, alq.KMInicio, alq.KMFin, alq.CombustibleFin, alq.Observaciones "
                    + "FROM (SELECT codAlquiler FROM " + nameBD + ".Alquiler WHERE codAlquiler "
                    + "NOT IN (SELECT codAlquiler FROM " + nameBD + ".AlquilerFactura)) AlqNotFact, "
                    + nameBD + ".Alquiler alq "
                    + "WHERE alq.codCliente=? AND AlqNotFact.codAlquiler=alq.codAlquiler AND alq.FechaEntrega IS NOT NULL");
            select.setString(1, cli.getCodCliente());
            rs = select.executeQuery();
            alquileresCliente = new HashMap<String, Alquiler>();
            while (rs.next()) {
                String codAlquiler = rs.getString("alq.codAlquiler");
                Vehiculo vehicle = this.getVehiculo("codVehiculo", rs.getString("alq.codVehiculo"), null);
                Tarifa tarif = this.getTarifa(rs.getString("alq.codTarifa"), null);
                if (vehicle != null && tarif != null) {
                    Alquiler alq = new Alquiler(codAlquiler, cli, vehicle, tarif, rs.getDate("alq.FechaInicio"), rs.getDate("alq.FechaFin"), rs.getDate("alq.FechaEntrega"), rs.getBigDecimal("alq.Importe"), rs.getInt("alq.KMInicio"), rs.getInt("alq.KMFin"), rs.getInt("alq.CombustibleFin"), rs.getString("alq.Observaciones"));
                    alquileresCliente.put(codAlquiler, alq);
                } else {
                    alquileresCliente.clear();
                    break;
                }

            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo alquileres de un cliente pendientes de facturar", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (alquileresCliente.isEmpty()) {
            return null;
        }
        return alquileresCliente;

    }

    @Override
    public HashMap<String, Incidencia> getIncidenciasClienteSinFacturar(Cliente cli) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Incidencia> incidenciasCliente = new HashMap<String, Incidencia>();
        HashMap<String, TipoIncidencia> tiposIncidencias = new HashMap<String, TipoIncidencia>();
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT inc.codIncidencia, inc.codTipoIncidencia, inc.codAlquiler, inc.codCliente, inc.Fecha, "
                    + "inc.Observaciones, inc.Precio "
                    + "FROM (SELECT codIncidencia FROM " + nameBD + ".Incidencia WHERE codIncidencia "
                    + "NOT IN (SELECT codIncidencia FROM " + nameBD + ".IncidenciaFactura)) IncNotFact, "
                    + nameBD + ".Incidencia inc, " + nameBD + ".TipoIncidencia tipoinc "
                    + "WHERE inc.codCliente=? AND tipoinc.codTipoIncidencia=inc.codTipoIncidencia "
                    + "AND tipoinc.AbonaCliente=? AND IncNotFact.codIncidencia=inc.codIncidencia");
            select.setString(1, cli.getCodCliente());
            select.setBoolean(2, true);
            rs = select.executeQuery();
            incidenciasCliente = new HashMap<String, Incidencia>();
            while (rs.next()) {
                String codIncidencia = rs.getString("inc.codIncidencia");
                String codTipoIncidencia = rs.getString("inc.codTipoIncidencia");
                ///////////EVITANDO CONSULTAS IGUALES A TIPO INCIDENCIA ///////////
                TipoIncidencia tipoIncidencia;
                if (tiposIncidencias.containsKey(codTipoIncidencia)) {
                    tipoIncidencia = tiposIncidencias.get(codTipoIncidencia);
                } else {
                    tipoIncidencia = this.getTipoInciencia(rs.getString("inc.codTipoIncidencia"), null);
                    tiposIncidencias.put(codTipoIncidencia, tipoIncidencia);
                }
                ///////////FIN EVITACION CONSULTAS IGUALES ///////////
                if (tipoIncidencia != null) {
                    Incidencia inc = new Incidencia(codIncidencia, tipoIncidencia, rs.getString("inc.codAlquiler"),
                            rs.getString("inc.codCliente"), rs.getDate("Fecha"), rs.getString("inc.Observaciones"),
                            rs.getBigDecimal("inc.Precio"));
                    incidenciasCliente.put(codIncidencia, inc);
                } else {
                    incidenciasCliente.clear();
                    break;
                }

            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo incidencias de un cliente pendientes de facturar", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (incidenciasCliente.isEmpty()) {
            return null;
        }
        return incidenciasCliente;
    }

    @Override
    public HashMap<String, Empleado> getEmpleados(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Empleado> empleados = new HashMap<String, Empleado>();
        try {
            conexion = pool.getConnection();
            if (campo == null && valor == null) {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Empleado");
            } else {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Empleado WHERE " + campo + "=?");
                select.setString(1, valor);
            }
            rs = select.executeQuery();
            while (rs.next()) {
                Empleado empl = new Empleado(rs.getString("codEmpleado"), rs.getString("Nombre"), rs.getString("UserName"),
                        rs.getString("Pass"), rs.getString("DNI"), rs.getString("Telefono"), rs.getString("Direccion"),
                        rs.getString("codSucursal"), rs.getString("Permisos").charAt(0));
                empleados.put(empl.getCodEmpleado(), empl);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error consultando los empleados de la base de datos", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (empleados.isEmpty()) {
            return null;
        }
        return empleados;
    }

    @Override
    public HashMap<String, Sucursal> getSucursales(boolean central) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Sucursal> sucursales = new HashMap<String, Sucursal>();
        try {
            conexion = pool.getConnection();
            if (central) {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Sucursal WHERE Central=?");
                select.setBoolean(1, true);
            } else {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Sucursal");
            }
            rs = select.executeQuery();
            while (rs.next()) {
                Sucursal suc = new Sucursal(rs.getString("codSucursal"), rs.getString("Nombre"), rs.getString("Direccion"), rs.getString("Telefono"), rs.getString("Fax"), rs.getBoolean("Central"));
                sucursales.put(suc.getCodSucursal(), suc);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error consultando las sucursales de la base de datos", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (sucursales.isEmpty()) {
            return null;
        }
        return sucursales;
    }

    @Override
    public HashMap<String, Alquiler> getAlquileres(String campo, String valor, String codSucursal, Boolean finalizado) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Alquiler> alquileres = new HashMap<String, Alquiler>();
        try {
            conexion = pool.getConnection();
            if (campo != null && valor != null && finalizado == null) {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Alquiler alq WHERE alq." + campo + "=?");
                select.setString(1, valor);
            } else if (campo != null && valor != null && finalizado != null) {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Alquiler alq "
                        + "WHERE alq.FechaEntrega IS NULL AND alq." + campo + "=?");
                select.setString(1, valor);
            } else if (campo == null && valor == null && codSucursal != null) {
                select = conexion.prepareStatement("SELECT alq.codAlquiler, alq.codCliente, alq.codVehiculo, alq.codTarifa, alq.FechaInicio, "
                        + "alq.FechaFin, alq.FechaEntrega, alq.Importe, alq.KMInicio, alq.KMFin, alq.CombustibleFin, alq.Observaciones "
                        + "FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Cliente cli "
                        + "WHERE cli.codSucursal=? AND alq.codCliente=cli.codCliente");
                select.setString(1, codSucursal);
            } else {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Alquiler alq");
            }
            rs = select.executeQuery();
            while (rs.next()) {
                String codAlquiler = rs.getString("alq.codAlquiler");
                Cliente cliAlquiler = this.getClient(rs.getString("alq.codCliente"));
                Vehiculo vehiculo = this.getVehiculo("codVehiculo", rs.getString("alq.codVehiculo"), null);
                Tarifa tarifa = this.getTarifa(rs.getString("alq.codTarifa"), null);
                Alquiler alquiler = new Alquiler(codAlquiler, cliAlquiler, vehiculo, tarifa, rs.getDate("alq.FechaInicio"), rs.getDate("alq.FechaFin"), rs.getDate("alq.FechaEntrega"), rs.getBigDecimal("alq.Importe"), rs.getInt("alq.KMInicio"), rs.getInt("alq.KMFin"), rs.getInt("alq.combustibleFin"), rs.getString("alq.Observaciones"));
                alquileres.put(codAlquiler, alquiler);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error consultando alquileres de la base de datos", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (alquileres.isEmpty()) {
            return null;
        }
        return alquileres;
    }

    @Override
    public Factura generarFactura(Sucursal suc, Cliente cli, String[] alquileres, String[] incidencias, HttpServletRequest request) {
        Connection conexion = null;
        PreparedStatement selectAlquiler = null;
        PreparedStatement selectIncidencia = null;
        PreparedStatement insertFactura = null;
        PreparedStatement insertElementosFactura = null;
        ResultSet rsAlquiler = null;
        ResultSet rsIncidencia = null;
        HashMap<String, Alquiler> alquileresFacturar = new HashMap<String, Alquiler>();
        HashMap<String, Incidencia> incidenciasFacturar = new HashMap<String, Incidencia>();
        HashMap<String, TipoIncidencia> tiposIncidencia = new HashMap<String, TipoIncidencia>();
        Factura factura = null;
        boolean okAlq = true;
        boolean okInc = true;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            conexion.setAutoCommit(false);
            selectAlquiler = conexion.prepareStatement("SELECT alq.codAlquiler, alq.codCliente, alq.codVehiculo, alq.codTarifa, alq.FechaInicio, "
                    + "alq.FechaFin, alq.FechaEntrega, alq.Importe, alq.KMInicio, alq.KMFin, alq.CombustibleFin, alq.Observaciones "
                    + "FROM (SELECT codAlquiler FROM " + nameBD + ".Alquiler WHERE codAlquiler "
                    + "NOT IN (SELECT codAlquiler FROM " + nameBD + ".AlquilerFactura)) AlqNotFact, "
                    + nameBD + ".Alquiler alq "
                    + "WHERE alq.codAlquiler=? AND alq.FechaEntrega IS NOT NULL AND alq.codCliente=?");

            selectIncidencia = conexion.prepareStatement("SELECT inc.codIncidencia, inc.codTipoIncidencia, inc.codAlquiler, "
                    + "inc.codCliente, inc.Fecha, inc.Observaciones, inc.Precio "
                    + "FROM (SELECT codIncidencia FROM " + nameBD + ".Incidencia WHERE codIncidencia "
                    + "NOT IN (SELECT codIncidencia FROM " + nameBD + ".IncidenciaFactura)) IncNotFact, "
                    + nameBD + ".Incidencia inc "
                    + "WHERE inc.codIncidencia=? AND inc.codCliente=?");
            if (alquileres != null) {
                for (int i = 0; i < alquileres.length; i++) {
                    selectAlquiler.setString(1, alquileres[i]);
                    selectAlquiler.setString(2, cli.getCodCliente());
                    rsAlquiler = selectAlquiler.executeQuery();

                    while (rsAlquiler.next()) {
                        Vehiculo vehicle = this.getVehiculo("codVehiculo", rsAlquiler.getString("alq.codVehiculo"), conexion);
                        Tarifa tarifa = this.getTarifa(rsAlquiler.getString("alq.codTarifa"), conexion);
                        if (vehicle != null && tarifa != null) {
                            Alquiler alq = new Alquiler(rsAlquiler.getString("alq.codAlquiler"), cli, vehicle, tarifa,
                                    rsAlquiler.getDate("alq.FechaInicio"), rsAlquiler.getDate("alq.FechaFin"),
                                    rsAlquiler.getDate("alq.FechaEntrega"), rsAlquiler.getBigDecimal("alq.Importe"),
                                    rsAlquiler.getInt("alq.KMInicio"), rsAlquiler.getInt("alq.KMFin"),
                                    rsAlquiler.getInt("alq.CombustibleFin"), rsAlquiler.getString("alq.Observaciones"));
                            alquileresFacturar.put(alq.getCodAlquiler(), alq);
                        } else {
                            okAlq = false;
                        }
                    }
                    selectAlquiler.clearParameters();
                    rsAlquiler.close();
                }
            }
            if (incidencias != null) {
                for (int i = 0; i < incidencias.length; i++) {
                    selectIncidencia.setString(1, incidencias[i]);
                    selectIncidencia.setString(2, cli.getCodCliente());
                    rsIncidencia = selectIncidencia.executeQuery();
                    while (rsIncidencia.next()) {
                        TipoIncidencia tipoIncidencia = this.getTipoInciencia(rsIncidencia.getString("inc.codTipoIncidencia"), conexion);
                        if (tipoIncidencia != null) {
                            Incidencia inc = new Incidencia(rsIncidencia.getString("inc.codIncidencia"), tipoIncidencia,
                                    rsIncidencia.getString("inc.CodAlquiler"), cli.getCodCliente(),
                                    rsIncidencia.getDate("inc.Fecha"), rsIncidencia.getString("inc.Observaciones"),
                                    rsIncidencia.getBigDecimal("inc.Precio"));
                            incidenciasFacturar.put(inc.getCodIncidencia(), inc);
                        } else {
                            okInc = false;
                        }
                    }
                    selectIncidencia.clearParameters();
                    rsIncidencia.close();
                }
            }
            if (okAlq || okInc) {
                GenerateBill genBill = new GenerateBill(cli, alquileres, alquileresFacturar, incidencias, incidenciasFacturar, request);
                factura = genBill.generateBill();
                insertFactura = conexion.prepareStatement("INSERT INTO " + nameBD + ".Factura VALUES (?,?,?,?,?,?,?,?,?)");
                insertFactura.setString(1, factura.getCodFactura());
                insertFactura.setString(2, factura.getCliente().getCodCliente());
                insertFactura.setInt(3, factura.getIVA());
                insertFactura.setBigDecimal(4, factura.getImporteSinIVA());
                insertFactura.setBigDecimal(5, factura.getImporte());
                insertFactura.setDate(6, new java.sql.Date(factura.getFechaEmision().getTime()));
                insertFactura.setNull(7, java.sql.Types.VARCHAR);
                insertFactura.setNull(8, java.sql.Types.DATE);
                insertFactura.setBoolean(9, factura.isPagado());

                int filasTablaFactura = insertFactura.executeUpdate();
                if (filasTablaFactura != 1) {
                    conexion.rollback();
                }

                if (filasTablaFactura == 1 && factura.getAlquileres() != null) {
                    insertElementosFactura = conexion.prepareStatement("INSERT INTO " + nameBD + ".AlquilerFactura VALUES (?,?)");
                    for (Alquiler alq : factura.getAlquileres().values()) {
                        insertElementosFactura.setString(1, factura.getCodFactura());
                        insertElementosFactura.setString(2, alq.getCodAlquiler());
                        if (insertElementosFactura.executeUpdate() == 1) {
                            insertElementosFactura.clearParameters();
                        } else {
                            conexion.rollback();
                            break;
                        }
                    }
                }

                if (filasTablaFactura == 1 && factura.getIncidencias() != null) {
                    insertElementosFactura = conexion.prepareStatement("INSERT INTO " + nameBD + ".IncidenciaFactura VALUES (?,?)");
                    for (Incidencia inc : factura.getIncidencias().values()) {
                        insertElementosFactura.setString(1, factura.getCodFactura());
                        insertElementosFactura.setString(2, inc.getCodIncidencia());
                        if (insertElementosFactura.executeUpdate() == 1) {
                            insertElementosFactura.clearParameters();
                        } else {
                            conexion.rollback();
                            break;
                        }
                    }
                }
            }
            GeneratePDFBill pdfBill = new GeneratePDFBill(factura, suc, request.getServletContext().getRealPath("/"));
            if (pdfBill.generateBill()) {
                conexion.commit();
                ok = true;
            } else {
                conexion.rollback();
                request.setAttribute("resultados", "Factura no generada");
                Tools.anadirMensaje(request, "Ocurrio un error generando el documento de la factura", 'e');
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error creando una factura en la base de datos", ex);
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                logger.log(Level.SEVERE, "Error haciendo rollback de transacción de generación de factura", ex1);
            }
        } finally {
            cerrarResultSets(rsAlquiler, rsIncidencia);
            cerrarConexionesYStatement(conexion, selectAlquiler, selectIncidencia, insertFactura, insertElementosFactura);
        }
        if (!ok) {
            return null;
        }
        return factura;
    }

    @Override
    public HashMap<String, Factura> getFacturasPendientesPago(String codCliente, String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Factura> facturas = new HashMap<String, Factura>();
        try {
            conexion = pool.getConnection();
            if (codCliente != null) {
                select = conexion.prepareStatement("SELECT codFactura FROM " + nameBD + ".Factura WHERE codCliente=? AND Pagado=?");
                select.setString(1, codCliente);
                select.setBoolean(2, false);
            } else if (codCliente == null && codSucursal != null) {
                select = conexion.prepareStatement("SELECT codFactura FROM " + nameBD + ".Factura fac, " + nameBD + ".Cliente cli "
                        + "WHERE fac.codCliente=cli.codCliente AND cli.codSucursal=? AND Pagado=?");
                select.setString(1, codSucursal);
                select.setBoolean(2, false);
            } else {
                select = conexion.prepareStatement("SELECT codFactura FROM " + nameBD + ".Factura WHERE AND Pagado=?");
                select.setBoolean(1, false);
            }
            rs = select.executeQuery();
            facturas = new HashMap<String, Factura>();
            while (rs.next()) {
                Factura fact = this.getFactura(rs.getString("codFactura"), conexion);
                facturas.put(fact.getCodFactura(), fact);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error obteniendo las facturas pendientes de pago", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (facturas.isEmpty()) {
            return null;
        }
        return facturas;
    }

    @Override
    public HashMap<String, Factura> getFacturas(String campo, String valor, String codSucursal) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Factura> facturas = new HashMap<String, Factura>();
        try {
            conexion = pool.getConnection();
            if (codSucursal == null) {
                select = conexion.prepareStatement("SELECT codFactura FROM " + nameBD + ".Factura WHERE " + campo + "=?");
                select.setString(1, valor);
            } else {
                if (!campo.equals("1")) {
                    select = conexion.prepareStatement("SELECT fac.codFactura "
                            + "FROM " + nameBD + ".Factura fac, " + nameBD + ".Cliente cli "
                            + "WHERE fac.codCliente=cli.codCliente AND cli.codSucursal=? AND fac." + campo + "=?");
                    select.setString(1, codSucursal);
                    select.setString(2, valor);
                } else {
                    select = conexion.prepareStatement("SELECT fac.codFactura "
                            + "FROM " + nameBD + ".Factura fac, " + nameBD + ".Cliente cli "
                            + "WHERE fac.codCliente=cli.codCliente AND cli.codSucursal=?");
                    select.setString(1, codSucursal);
                }
            }


            rs = select.executeQuery();
            facturas = new HashMap<String, Factura>();
            while (rs.next()) {
                Factura fact = this.getFactura(rs.getString("codFactura"), conexion);
                if (fact != null) {
                    facturas.put(fact.getCodFactura(), fact);
                } else {
                    logger.log(Level.SEVERE, "Ha ocurrido un error obteniendo una factura en getFacturas ()");
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error obteniendo una lista de facturas", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (facturas.isEmpty()) {
            return null;
        }
        return facturas;
    }

    @Override
    public boolean pagarFactura(String codFactura, java.util.Date fechaPago, String formaPago) {
        Connection conexion = null;
        PreparedStatement update = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            update = conexion.prepareStatement("UPDATE " + nameBD + ".Factura "
                    + "SET FechaPago=?, FormaPago=?, Pagado=? "
                    + "WHERE codFactura=? AND Pagado=?");
            update.setDate(1, new java.sql.Date(fechaPago.getTime()));
            update.setString(2, formaPago);
            update.setBoolean(3, true);
            update.setString(4, codFactura);
            update.setBoolean(5, false);
            if (update.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al actualizar el estado de una factura a pagada en la Base de Datos", ex);
        } finally {
            cerrarConexionesYStatement(conexion, update);
        }
        return ok;
    }

    @Override
    public boolean editClient(String codCliente, Cliente client) {
        Connection conexion = null;
        PreparedStatement update = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            update = conexion.prepareStatement("UPDATE " + nameBD + ".Cliente SET Nombre=?, Edad=?, Empresa=?, Direccion=?, "
                    + "Telefono=?, Email=? WHERE codCliente=?");
            update.setString(1, client.getName());
            update.setInt(2, client.getAge());
            update.setString(3, client.getCompany());
            update.setString(4, client.getAddress());
            update.setString(5, client.getTelephone());
            update.setString(6, client.getEmail());
            update.setString(7, codCliente);
            if (update.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error editando cliente en la base de datos", ex);
        } finally {
            cerrarConexionesYStatement(conexion, update);
        }
        return ok;
    }

    @Override
    public boolean deleteClient(String codCliente) {
        Connection conexion = null;
        PreparedStatement delete = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            delete = conexion.prepareStatement("UPDATE " + nameBD + ".Cliente SET Activo=? WHERE codCliente=? AND Activo=?");
            delete.setBoolean(1, false);
            delete.setString(2, codCliente);
            delete.setBoolean(3, true);
            if (delete.executeUpdate() <= 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error borrando cliente de la base de datos", ex);
        } finally {
            cerrarConexionesYStatement(conexion, delete);
        }
        return ok;
    }

    @Override
    public HashMap<String, Vehiculo> getVehiclesForRent(String codSucursal, java.util.Date fechaInicio, java.util.Date fechaFin, String codVehiculo, Connection conExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Vehiculo> vehiculos = new HashMap<String, Vehiculo>();
        try {
            if (conExterna == null) {
                conexion = pool.getConnection();
            } else {
                conexion = conExterna;
            }
            if (codVehiculo == null) {
                select = conexion.prepareStatement("SELECT ve.codVehiculo "
                        + "FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.codSucursal=? AND ?>? "
                        + "AND(ve.codVehiculo IN (SELECT * FROM " + nameBD + ".VDisponibleNoAlquilado) "
                        + "OR (ve.codVehiculo=alq.codVehiculo AND alq.codVehiculo IN (SELECT * FROM " + nameBD + ".VDisponibleAlqFinalizado)) "
                        + "OR (ve.codVehiculo=alq.codVehiculo AND alq.FechaEntrega IS NULL "
                        + "AND ((?>alq.FechaFin AND ?>alq.FechaFin) "
                        + "OR (?<alq.FechaInicio AND ?<alq.FechaInicio)))) "
                        + "AND ve.codVehiculo NOT IN (SELECT ve.codVehiculo FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.codSucursal=? AND ve.codVehiculo=alq.codVehiculo "
                        + "AND alq.FechaEntrega IS NULL "
                        + "AND ((? BETWEEN alq.FechaInicio AND alq.FechaFin)OR(? BETWEEN alq.FechaInicio AND alq.FechaFin))) "
                        + "GROUP BY ve.codVehiculo");
                select.setString(1, codSucursal);

                select.setDate(2, new java.sql.Date(fechaFin.getTime()));
                select.setDate(3, new java.sql.Date(fechaInicio.getTime()));

                select.setDate(4, new java.sql.Date(fechaFin.getTime()));
                select.setDate(5, new java.sql.Date(fechaInicio.getTime()));

                select.setDate(6, new java.sql.Date(fechaFin.getTime()));
                select.setDate(7, new java.sql.Date(fechaInicio.getTime()));

                select.setString(8, codSucursal);
                select.setDate(9, new java.sql.Date(fechaInicio.getTime()));
                select.setDate(10, new java.sql.Date(fechaFin.getTime()));

            } else {
                select = conexion.prepareStatement("SELECT ve.codVehiculo "
                        + "FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.codSucursal=? AND ve.codVehiculo=? AND ?>? "
                        + "AND(ve.codVehiculo IN (SELECT * FROM " + nameBD + ".VDisponibleNoAlquilado) "
                        + "OR (ve.codVehiculo=alq.codVehiculo AND alq.codVehiculo IN (SELECT * FROM " + nameBD + ".VDisponibleAlqFinalizado)) "
                        + "OR (ve.codVehiculo=alq.codVehiculo AND alq.FechaEntrega IS NULL "
                        + "AND ((?>alq.FechaFin AND ?>alq.FechaFin) "
                        + "OR (?<alq.FechaInicio AND ?<alq.FechaInicio)))) "
                        + "AND ve.codVehiculo NOT IN (SELECT ve.codVehiculo FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.codSucursal=? AND ve.codVehiculo=alq.codVehiculo "
                        + "AND alq.FechaEntrega IS NULL "
                        + "AND ((? BETWEEN alq.FechaInicio AND alq.FechaFin)OR(? BETWEEN alq.FechaInicio AND alq.FechaFin))) "
                        + "GROUP BY ve.codVehiculo");
                select.setString(1, codSucursal);
                select.setString(2, codVehiculo);

                select.setDate(3, new java.sql.Date(fechaFin.getTime()));
                select.setDate(4, new java.sql.Date(fechaInicio.getTime()));

                select.setDate(5, new java.sql.Date(fechaFin.getTime()));
                select.setDate(6, new java.sql.Date(fechaInicio.getTime()));

                select.setDate(7, new java.sql.Date(fechaFin.getTime()));
                select.setDate(8, new java.sql.Date(fechaInicio.getTime()));

                select.setString(9, codSucursal);
                select.setDate(10, new java.sql.Date(fechaInicio.getTime()));
                select.setDate(11, new java.sql.Date(fechaFin.getTime()));
            }

            rs = select.executeQuery();
            while (rs.next()) {
                Vehiculo vehicle = this.getVehiculo("codVehiculo", rs.getString("ve.codVehiculo"), conexion);
                if (vehicle != null) {
                    vehiculos.put(vehicle.getCodVehiculo(), vehicle);
                } else {
                    vehiculos.clear();
                    break;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ocurrio un error obteniendo vehiculos disponibles para alquiler", ex);
        } finally {
            if (conExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        if (vehiculos.isEmpty()) {
            return null;
        }
        return vehiculos;
    }

    @Override
    public boolean newRent(HttpServletRequest request, String codSucursal, String codCliente, String codVehiculo, java.util.Date fechaInicio, java.util.Date fechaFin, String codTarifa, int KMInicio) {
        Connection conexion = null;
        PreparedStatement insert = null;
        PreparedStatement selectKM = null;
        ResultSet rs = null;
        Boolean ok = false;
        Integer KMFinUltimoAlquiler = null;
        try {
            conexion = pool.getConnection();
            conexion.setAutoCommit(false);
            HashMap<String, Vehiculo> vehiculos = this.getVehiclesForRent(codSucursal, fechaInicio, fechaFin, codVehiculo, conexion);
            selectKM = conexion.prepareStatement("SELECT KMFin FROM " + nameBD + ".Alquiler "
                    + "WHERE FechaEntrega IS NOT NULL AND codVehiculo=? "
                    + "ORDER BY FechaEntrega DESC LIMIT 1");
            selectKM.setString(1, codVehiculo);
            rs = selectKM.executeQuery();
            while (rs.next()) {
                KMFinUltimoAlquiler = rs.getInt("KMFin");
            }

            if (vehiculos == null) {
                request.setAttribute("resultados", "Vehículo no disponible");
                Tools.anadirMensaje(request, "El vehículo seleccionado ha dejado de estar disponible", 'w');
                conexion.rollback();
            } else if (KMFinUltimoAlquiler != null && KMInicio < KMFinUltimoAlquiler) {
                request.setAttribute("resultados", "Kilómetros de inicio incorrectos");
                Tools.anadirMensaje(request, "Ha introducido unos kilómetros de inicio menores que con los que se entregó la última vez", 'w');
                conexion.rollback();
            } else {
                insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Alquiler VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                insert.setString(1, Tools.generaUUID());
                insert.setString(2, codCliente);
                insert.setString(3, codVehiculo);
                insert.setString(4, codTarifa);
                insert.setDate(5, new java.sql.Date(fechaInicio.getTime()));
                insert.setDate(6, new java.sql.Date(fechaFin.getTime()));
                insert.setNull(7, java.sql.Types.DATE);
                insert.setNull(8, java.sql.Types.DECIMAL);
                insert.setInt(9, KMInicio);
                insert.setNull(10, java.sql.Types.INTEGER);
                insert.setNull(11, java.sql.Types.INTEGER);
                insert.setNull(12, java.sql.Types.OTHER);
                if (insert.executeUpdate() == 1) {
                    ok = true;
                    conexion.commit();
                } else {
                    conexion.rollback();
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error insertando nuevo alquiler en la base de datos", ex);
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                logger.log(Level.SEVERE, "Error haciendo rollback de transacción de nuevo alquiler", ex1);
            }
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, insert, selectKM);
        }
        if (!ok) {
            request.setAttribute("resultados", "Fallo de alta");
            Tools.anadirMensaje(request, "Se ha producido un error añadiendo el nuevo alquiler", 'e');
        }
        return ok;
    }

    @Override
    public boolean endRent(Alquiler alq, java.util.Date fechaFin, java.util.Date fechaEntrega, int KMFin, int combustibleFin, String observaciones) {
        Connection conexion = null;
        PreparedStatement update = null;
        boolean ok = false;
        CalculateRentPrice calculator = new CalculateRentPrice(alq, fechaFin, fechaEntrega, combustibleFin);
        BigDecimal importe = calculator.calculateRentPrice();
        try {
            conexion = pool.getConnection();
            update = conexion.prepareStatement("UPDATE " + nameBD + ".Alquiler "
                    + "SET FechaFin=?, FechaEntrega=?, KMFin=?, CombustibleFin=?, Observaciones=?, Importe=? "
                    + "WHERE codAlquiler=? AND FechaEntrega IS NULL");
            update.setDate(1, new java.sql.Date(fechaFin.getTime()));
            update.setDate(2, new java.sql.Date(fechaEntrega.getTime()));
            update.setInt(3, KMFin);
            update.setInt(4, combustibleFin);
            update.setString(5, observaciones);
            update.setBigDecimal(6, importe);
            update.setString(7, alq.getCodAlquiler());
            if (update.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error finalizando alquiler en la base de datos", ex);
        } finally {
            cerrarConexionesYStatement(conexion, update);
        }
        return ok;
    }

    @Override
    public boolean addInciencia(Incidencia inc) {
        Connection conexion = null;
        PreparedStatement insert = null;
        boolean ok = false;
        try {
            conexion = pool.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + nameBD + ".Incidencia VALUES (?,?,?,?,?,?,?)");
            insert.setString(1, inc.getCodIncidencia());
            insert.setString(2, inc.getTipoIncidencia().getCodTipoIncidencia());
            insert.setString(3, inc.getCodAlquiler());
            insert.setString(4, inc.getCodCliente());
            insert.setDate(5, new java.sql.Date(inc.getFecha().getTime()));
            insert.setString(6, inc.getObservaciones());
            insert.setBigDecimal(7, inc.getPrecio());

            if (insert.executeUpdate() == 1) {
                ok = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error en alta de incidencia en la base de datos", ex);
        } finally {
            cerrarConexionesYStatement(conexion, insert);
        }
        return ok;
    }

    @Override
    public HashMap<String, Alquiler> getAlquileresPosiblesIncidencia(java.util.Date fecha, String matricula, String codAlquiler, Connection conexionExterna) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Alquiler> alquileres = new HashMap<String, Alquiler>();
        try {
            if (conexionExterna == null) {
                conexion = pool.getConnection();
            } else {
                conexion = conexionExterna;
            }
            if (codAlquiler == null) {
                select = conexion.prepareStatement("SELECT alq.codAlquiler "
                        + "FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.Matricula=? AND alq.codVehiculo=ve.codVehiculo "
                        + "AND ((? BETWEEN alq.FechaInicio AND alq.FechaFin) OR (? BETWEEN alq.FechaInicio AND alq.FechaEntrega))");
                select.setString(1, matricula);
                select.setDate(2, new java.sql.Date(fecha.getTime()));
                select.setDate(3, new java.sql.Date(fecha.getTime()));
            } else {
                select = conexion.prepareStatement("SELECT alq.codAlquiler "
                        + "FROM " + nameBD + ".Alquiler alq, " + nameBD + ".Vehiculo ve "
                        + "WHERE ve.Matricula=? AND alq.codVehiculo=ve.codVehiculo AND alq.codAlquiler=? "
                        + "AND ((? BETWEEN alq.FechaInicio AND alq.FechaFin) OR (? BETWEEN alq.FechaInicio AND alq.FechaEntrega))");
                select.setString(1, matricula);
                select.setString(2, codAlquiler);
                select.setDate(3, new java.sql.Date(fecha.getTime()));
                select.setDate(4, new java.sql.Date(fecha.getTime()));
            }
            rs = select.executeQuery();
            while (rs.next()) {
                Alquiler alq = this.getAlquiler(rs.getString("alq.codAlquiler"), conexion);
                if (alq != null) {
                    alquileres.put(alq.getCodAlquiler(), alq);
                } else {
                    alquileres.clear();
                    break;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo los alquileres posibles para una incidencia", ex);
        } finally {
            if (conexionExterna == null) {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(conexion, select);
            } else {
                cerrarResultSets(rs);
                cerrarConexionesYStatement(null, select);
            }
        }
        if (alquileres.isEmpty()) {
            return null;
        }
        return alquileres;
    }

    @Override
    public HashMap<String, TipoIncidencia> getTiposIncidencia() {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, TipoIncidencia> tiposIncidencias = new HashMap<String, TipoIncidencia>();
        try {
            conexion = pool.getConnection();
            select = conexion.prepareStatement("SELECT * FROM " + nameBD + ".TipoIncidencia");
            rs = select.executeQuery();
            while (rs.next()) {
                TipoIncidencia tipoIncidencia = new TipoIncidencia(rs.getString("codTipoIncidencia"), rs.getString("Nombre"), rs.getString("Descripcion"), rs.getBoolean("AbonaCliente"));
                tiposIncidencias.put(tipoIncidencia.getCodTipoIncidencia(), tipoIncidencia);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo los tipos de incidencia", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (tiposIncidencias.isEmpty()) {
            return null;
        }
        return tiposIncidencias;
    }

    @Override
    public HashMap<String, Tarifa> getTarifas(String campo, String valor) {
        Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        HashMap<String, Tarifa> tarifas = new HashMap<String, Tarifa>();
        try {
            conexion = pool.getConnection();
            if (campo == null && valor == null) {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Tarifa");
            } else {
                select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Tarifa WHERE " + campo + "=?");
                select.setString(1, valor);
            }
            rs = select.executeQuery();
            while (rs.next()) {
                Tarifa tarif = new Tarifa(rs.getString("codTarifa"), rs.getString("Nombre"), rs.getString("Descripcion"), rs.getBigDecimal("PrecioBase"), rs.getBigDecimal("PrecioDia"), rs.getBigDecimal("PrecioDiaExtra"), rs.getBigDecimal("PrecioCombustible"));
                tarifas.put(tarif.getCodTarifa(), tarif);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error insertando nuevo alquiler en la base de datos", ex);
        } finally {
            cerrarResultSets(rs);
            cerrarConexionesYStatement(conexion, select);
        }
        if (tarifas.isEmpty()) {
            return null;
        }
        return tarifas;
    }

    private void cerrarConexionesYStatement(Connection conexion, Statement... st) {
        for (Statement statement : st) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error al cerrar un statement", ex);
                }
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al cerrar una conexion", ex);
            }
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
