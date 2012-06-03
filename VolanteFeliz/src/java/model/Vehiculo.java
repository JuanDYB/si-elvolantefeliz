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
    private String rutaImagen;
    private int CapacidadCombustible;
    private String codSucursal;
    private String codTipoVehiculo;
    private String codRevision;
    private String codITV;

    public Vehiculo(String codVehiculo, String matricula, String marca, String modelo, String nBastidor, String rutaImagen, int CapacidadCombustible, String codSucursal, String codTipoVehiculo, String codRevision, String codITV) {
        this.codVehiculo = codVehiculo;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.nBastidor = nBastidor;
        this.rutaImagen = rutaImagen;
        this.CapacidadCombustible = CapacidadCombustible;
        this.codSucursal = codSucursal;
        this.codTipoVehiculo = codTipoVehiculo;
        this.codRevision = codRevision;
        this.codITV = codITV;
    }

    public int getCapacidadCombustible() {
        return CapacidadCombustible;
    }

    public String getCodITV() {
        return codITV;
    }

    public String getCodRevision() {
        return codRevision;
    }

    public String getCodSucursal() {
        return codSucursal;
    }

    public String getCodTipoVehiculo() {
        return codTipoVehiculo;
    }

    public String getCodVehiculo() {
        return codVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public String getnBastidor() {
        return nBastidor;
    }
    
    public String getRutaImagen() {
        return rutaImagen;
    }
}
