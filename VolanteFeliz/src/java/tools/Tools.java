package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
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
    
    public static void anadirMensaje(HttpServletRequest request, String msg) {
        if (request.getAttribute("listaResultados") == null) {
            ArrayList<String> lista = new ArrayList<String>();
            lista.add(msg);
            request.setAttribute("listaResultados", lista);
        } else {
            ArrayList<String> lista = (ArrayList<String>) request.getAttribute("listaResultados");
            lista.add(msg);
//            request.setAttribute("listaResultados", lista);
        }
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
        return validador.getValidInteger(context, input, 0, Integer.MAX_VALUE, false);
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
        return validador.getValidInput("Telefono/Fax", input, "Tlf", 14, false);
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
    
    public static String validateHost (String input) throws IntrusionException, ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("HOST", input, "Host", 1, false);
    }
    
    public static String validateBool (String input) throws ValidationException{
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Booleano", input, "Bool", 5, false);
//        if ((Boolean)Boolean.getBoolean(input) == null){
//            throw new ValidationException ("Cadena de entrada no convertible a booleano", "Cadena de entrada no convertible a booleano");
//        }
    }
    
    public static String passForMD5 (String pass){
        StringBuilder passAdd = new StringBuilder (pass.toUpperCase());
        return pass + passAdd.reverse().toString();
    }
    
    public static boolean validatePermisos(char perm) {
        if (perm == 'a' || perm == 'c') {
            return true;
        } else {
            return false;
        }
    }
    
    public static Locale getLocale() {
        return new Locale("es", "ES");
    }
    
    public static String getDate() {
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        String[] fecha = cal.getTime().toString().split(" ");
        String[] hora = fecha[3].split(":");

        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " "
                + hora[0] + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }
    
    public static String printDate(String fechaString) {
        String[] fechaSeparada = fechaString.split("-");
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        cal.set(Integer.valueOf(fechaSeparada[0]), Integer.valueOf(fechaSeparada[1]) -1, Integer.valueOf(fechaSeparada[2]));
        String[] date = cal.getTime().toString().split(" ");
        return date[2] + "-" + date[1] + "-" + date[5];
    }
    
    public static String roundDouble(double input) {
        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        return format.format(input);
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
    
    public static boolean emailSend (HttpServletRequest request, String subject, String destination, String contenido){
        MailSender mailConfig = (MailSender) request.getServletContext().getAttribute("emailSender");
        Session mailSession = mailConfig.startSession((Authenticator) request.getServletContext().getAttribute("mailAuth"));
        MimeMessage mensaje = mailConfig.newMail(subject, destination, contenido, mailSession);
        
        if (mensaje == null) {
            request.setAttribute("resultados", "Error enviando mensaje");
            Tools.anadirMensaje(request, "No se pudo enviar su email, disculpe las molestias");
            return false;
        } else {
            boolean ok = mailConfig.sendEmail(mensaje, mailSession);

            if (ok == true) {
                Tools.anadirMensaje(request, "Se le ha enviado un email con los datos del registro");
                return true;
            } else {
                Tools.anadirMensaje(request, "No se puedo enviar el email con los datos del registro");
                return false;
            }
        }
    }
}