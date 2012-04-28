package tools;

import java.math.BigDecimal;
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

    public static void anadirMensaje(HttpServletRequest request, String msg, char severity) {
        request.setAttribute("severityResultados", severity);
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

    public static void validateUUID(String uuid) throws ValidationException {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Validacion UUID Fallida", "Validacion UUID Fallida");
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

    public static String validateName(String input, int longitud, String context, boolean vacio) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput(context, input, "Name", longitud, vacio);
    }

    public static String validateAdress(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Dirección", input, "Adress", 400, false);
    }

    public static int validateNumber(String input, String context, int max) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInteger(context, input, 0, max, false);
    }

    public static double validatePrice(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidDouble("Precio", input, 0, Double.MAX_VALUE, false);
    }

    public static String validateText(String input, int length, String context) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput(context, input, "NameDescProd", length, false);
    }

    public static String validatePhone(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Telefono/Fax", input, "Tlf", 14, false);
    }

    public static String validateUserName(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Nombre de usuario", input, "userName", 50, false);
    }

    public static String validateDNI(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("DNI", input, "DNI", 9, false);
    }

    public static String validatePerm(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Caracter de permisos", input, "Perm", 1, false);
    }

    public static String validateHost(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("HOST", input, "Host", Integer.MAX_VALUE, false);
    }

    public static String validateBool(String input) throws IntrusionException, ValidationException {
        Validator validador = ESAPI.validator();
        return validador.getValidInput("Booleano", input, "Bool", 5, false);
//        if ((Boolean)Boolean.getBoolean(input) == null){
//            throw new ValidationException ("Cadena de entrada no convertible a booleano", "Cadena de entrada no convertible a booleano");
//        }
    }

    public static String passForMD5(String pass) {
        StringBuilder passAdd = new StringBuilder(pass.toUpperCase());
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

    public static Date getDate() {
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance(Tools.getLocale());
        return cal.getTime();
    }

    public static String printDate(Date fecha) {
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        cal.setTime(fecha);
        return cal.get(GregorianCalendar.DAY_OF_MONTH) + "-" + cal.get(GregorianCalendar.MONTH) + "-" + cal.get(GregorianCalendar.YEAR);
    }

    public static String printDate(String fechaString) {
        String[] fechaSeparada = fechaString.split("-");
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        cal.set(Integer.valueOf(fechaSeparada[0]), Integer.valueOf(fechaSeparada[1]) - 1, Integer.valueOf(fechaSeparada[2]));
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
        Scanner sc = null;
        boolean ok = false;
        try {
            sc = new Scanner(Tools.class.getResourceAsStream(ruta), "UTF-8");


            while (sc.hasNext()) {
                texto.append(sc.nextLine());
                texto.append("\n");
            }
            ok = true;
        } catch (NullPointerException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, "Plantilla Mail no encontrada");
        }finally{
            if (sc != null){
                sc.close();
            }
        }
        if (!ok){
            return null;
        }
        return texto.toString();
    }

    public static String printBigDecimal(BigDecimal input) {
        NumberFormat numFormat = NumberFormat.getInstance(Tools.getLocale());
        return numFormat.format(input);
    }

    public static boolean emailSend(HttpServletRequest request, String subject, String destination, String contenido, HashMap<String, String> adjuntos) {
        MailSender mailConfig = (MailSender) request.getServletContext().getAttribute("emailSender");
        Session mailSession = null;
        if (mailConfig == null){
            return false;
        }
        mailSession = mailConfig.startSession((Authenticator) request.getServletContext().getAttribute("mailAuth"));
        if (mailSession == null){
            return false;
        }

        MimeMessage mensaje = null;
        if (adjuntos == null) {
            mensaje = mailConfig.newMail(subject, destination, contenido, mailSession);
        } else {
            mensaje = mailConfig.newMail(adjuntos, subject, destination, contenido, mailSession);
        }

        if (mensaje == null) {
            return false;
        } else {
            return mailConfig.sendEmail(mensaje, mailSession);
        }
    }
}
