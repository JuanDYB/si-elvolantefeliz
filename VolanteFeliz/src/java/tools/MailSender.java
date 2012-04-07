package tools;

import tools.Tools;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class MailSender {
    private Properties configSMTP;
    private String pass;

    public MailSender(HashMap <String, String> config) {
        Properties prop = System.getProperties();
        prop.setProperty("mail.smtp.host", config.get("smtp.host"));
        prop.setProperty("mail.smtp.starttls.enable", config.get("smtp.tsl"));
        prop.setProperty("mail.smtp.port", config.get("smtp.port"));
        prop.setProperty("mail.smtp.user", config.get("smtp.user"));
        prop.setProperty("mail.smtp.auth", config.get("smtp.auth"));
        prop.setProperty("mail.from", config.get("smtp.from"));  
        configSMTP = prop;
        pass = config.get("smtp.pass");
    }
    
    public Authenticator getAuth (){
        return new SMTPAuthenticator();
    }
    
    public Session startSession (Authenticator auth){
        Session sesion = Session.getDefaultInstance(configSMTP, auth);
        return sesion;
    }
    
    public MimeMessage newMail (String subject, String to, String content, Session sesion){
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        try {
            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mensaje.setSentDate(cal.getTime());
            mensaje.setFrom(new InternetAddress(configSMTP.getProperty("mail.from")));
            mensaje.addHeader( "Content-Type", "text/html; charset=UTF-8" );
            mensaje.setSubject(subject, "UTF-8");            
            mensaje.setText(content, "UTF-8", "html");
            return mensaje;
        } catch (AddressException ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.WARNING, "Dirección de destino no válida", ex);
            return null;
        } catch (MessagingException ex){
            Logger.getLogger(MailSender.class.getName()).log(Level.WARNING, "Error creando el mensaje", ex);
            return null;
        }    
    }
    
    public boolean sendEmail (Message mensaje, Session sesion){
        Transport transporte = null;
        try {
            transporte = sesion.getTransport("smtp");
            transporte.connect(configSMTP.getProperty("mail.smtp.user"), pass);
            transporte.sendMessage(mensaje, mensaje.getAllRecipients());
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, "Error conectando con SMTP", ex);
            return false;
        }
        finally{
            if(transporte != null)  {
                try {
                    transporte.close();
                } catch (MessagingException ex) {
                    Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
    @Override
        public PasswordAuthentication getPasswordAuthentication() {
           String user = configSMTP.getProperty("mail.smtp.user");
           return new PasswordAuthentication(user, pass);
        }
    }
}
