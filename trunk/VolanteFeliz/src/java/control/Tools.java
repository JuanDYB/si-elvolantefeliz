package control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class Tools {
    public static String generateMD5Signature(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] huella = md.digest(input.getBytes());
            return transformaAHexadecimal(huella);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE,
                    "No se ha encontrado el algoritmo MD5", ex);
            return "-1";
        }
    }

    private static String transformaAHexadecimal(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }
    
    public static String generaUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean validateUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
    
    public static String validateEmail(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Email", input, "Email", 60, false);
    }

    public static String validatePass(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Contraseña", input, "Pss", 20, false);
    }

    public static String validateName(String input, int longitud) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Nombre", input, "Name", longitud, false);
    }

    public static String validateAdress(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Dirección", input, "Adress", 400, false);
    }

    public static int validateNumber(String input, String context)
            throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInteger(context, input, 0, 999999, false);
    }

    public static double validatePrice(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidDouble("Precio", input, 0, Double.MAX_VALUE, false);
    }

    public static String validateText(String input, int length, String context)
            throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput(context, input, "NameDescProd", length, false);
    }
    
    public static String validatePhone (String input) throws IntrusionException, ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Teléfono/Fax", input, "Tlf", 14, false);
    }
    
    public static String validateUserName (String input) throws IntrusionException, ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Nombre de usuario", input, "userName", 50, false);
    }
    
    public static String validateDNI (String input) throws IntrusionException, ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("DNI", input, "DNI", 9, false);
    }
    
    public static String validatePerm (String input) throws IntrusionException, ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Caracter de permisos", input, "Perm", 1, false);
    }
    
    public static String validateBool (String input) throws ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Booleano", input, "Bool", 5, false);
//        if ((Boolean)Boolean.getBoolean(input) == null){
//            throw new ValidationException ("Cadena de entrada no convertible a booleano", "Cadena de entrada no convertible a booleano");
//        }
    }
    
    public static boolean validatePermisos(char perm) {
        if (perm == 'a' || perm == 'c') {
            return true;
        } else {
            return false;
        }
    }
    
    public static String leerArchivoClassPath(String ruta) {
        StringBuilder texto = new StringBuilder();
        Scanner sc = new Scanner(Tools.class.getResourceAsStream(ruta), "UTF-8");


        while (sc.hasNext()) {
            texto.append(sc.nextLine());
            texto.append("\n");
        }

        sc.close();


        return texto.toString();
    }
}
