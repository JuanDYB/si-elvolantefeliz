package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Empleado {
    private String codEmpleado;
    private String userName;
    private String name;
    private String dni;
    private String telephone;
    private String address;
    private String codSucursal;
    private char permisos;  //'a': Administrador   'e': Empleado normal

    public Empleado(String codEmpleado, String userName, String name, String dni, String telephone, String address, String codSucursal, char permisos) {
        this.codEmpleado = codEmpleado;
        this.userName = userName;
        this.name = name;
        this.dni = dni;
        this.telephone = telephone;
        this.address = address;
        this.codSucursal = codSucursal;
        this.permisos = permisos;
    }

    public String getAddress() {
        return address;
    }

    public String getCodEmpleado() {
        return codEmpleado;
    }

    public String getCodSucursal() {
        return codSucursal;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public char getPermisos() {
        return permisos;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getUserName() {
        return userName;
    }
    
}
