package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Cliente {
    private String codCliente;
    private String name;
    private String email;
    private String dni;
    private String address;
    private String telephone;
    private String company;
    private String codSucursal;
    private int age;

    public Cliente(String codCliente, String name, String email, String dni, String address, String telephone, String company, String codSucursal, int age) {
        this.codCliente = codCliente;
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.address = address;
        this.telephone = telephone;
        this.company = company;
        this.codSucursal = codSucursal;
        this.age = age;
    }
    
    public String getCodCliente() {
        return codCliente;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getCodSucursal() {
        return codSucursal;
    }

    public String getCompany() {
        return company;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }
    
}
