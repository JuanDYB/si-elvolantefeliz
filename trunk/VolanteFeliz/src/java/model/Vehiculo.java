package model;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Vehiculo {
    private String codVehiculo;
    private String matricula;
    private String marca;
    private String modelo;
    private String nBastidor;
    private int CapacidadCombustible;
    private String codSucursal;
    private String codTipoVehiculo;
    private String codRevision;
    private String codITV;

    public Vehiculo(String codVehiculo, String matricula, String marca, String modelo, String nBastidor, int CapacidadCombustible, String codSucursal, String codTipoVehiculo, String codRevision, String codITV) {
        this.codVehiculo = codVehiculo;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.nBastidor = nBastidor;
        this.CapacidadCombustible = CapacidadCombustible;
        this.codSucursal = codSucursal;
        this.codTipoVehiculo = codTipoVehiculo;
        this.codRevision = codRevision;
        this.codITV = codITV;
    }
}
