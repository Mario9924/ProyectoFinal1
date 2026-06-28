package mail;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import logica.Log;

/**
 * 
 * Esta clase permite el envío de correos en función del mensaje que se quiere dar, por ejemplo un correo de bienvenida
 *  o un correo de despedida si se da de baja el usuario.
 * Esta clase hace uso de la API de Jakarta Mail.
 * 
 * Esta clase necesita ser instanciada para poder ser utilizada
 * 
 * @author Mario Gutiérrez González
 */
public class EnvioJakartaMail {
    
    private static Session session;
    /**
     * Atributo de tipo final que almacena la dirección de email desde la que se envían los correos
     */
    final String username = "marioguti888@gmail.com";
    /**
     * Atributo de tipo final que almcena la contraseña del servicio Gmail para el envío de correos desde la API
     */
    final String password = "wvwwxgvfwlaicynq";

    Properties props = new Properties();

    public EnvioJakartaMail() {
        this.session = Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // Estos son los protocolos y puertos necesarios para el envío de correos
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     * Esta función permite enviar cualquier mensaje a cualquier destinatario.
     * @param destino String con la dirección de destino del usuario
     * @param asunto String con el asunto del correo
     * @param cuerpo String con el cuerpo del mensaje
     * @throws MessagingException Usado por obligación del IDE
     */
    public static void enviarEmail(String destino, String asunto, String cuerpo) throws MessagingException {

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress("marioguti888@gmail.com")); //dirección FROM
            //destinatarios normales           
            mensaje.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("marioguti888@gmail.com, " + destino));

            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Mensaje enviado correctamente");
        } catch (MessagingException e) {
            System.err.println("Ha ocurrido un error al enviar el mensaje: " + e);
            Log.escribirLog("Ha ocurrido un error al enviar el email: " + e);
        }
    }
    
    /**
     * Esta función permite enviar el correo de bienvenida al usuario
     * @param destino String con la dirección de destino del usuario que se ha dado de alta
     */
    public void correoBienvenida(String destino) {
        String cuerpo = "";
        try {
            cuerpo = "Bienvenido " + destino + "\n Este es un mensaje de bienvenida automático";
            enviarEmail(destino, "Bienvenido a la plataforma", cuerpo);
        } catch (MessagingException ex) {
            System.err.println("Ha ocurrido un error al enviar el mensaje: " + ex);
            Log.escribirLog("Ha ocurrido un error al enviar el email: " + ex);
        }
    }

    /**
     * Esta función permite enviar el correo de baja de la plataforma
     * @param destino String con la dirección de correo del usuario
     */
    public void correoUsuarioBaja(String destino) {
        String cuerpo = "";
        try {
            cuerpo = "Has decidido eliminar tu cuenta de correo. "
                    + "\nEn los próximos 15 días tu cuenta se eliminará por completo, a partir de hoy no tendrás acceso"
                    + "\nEn caso de querer cancelar esta acción ponte en contacto con nosotros en soporte@soporte.com."
                    + "\nUn saludo";
            enviarEmail(destino, "Baja de la plataforma", cuerpo);
        } catch (MessagingException ex) {
            System.err.println("Ha ocurrido un error al enviar el mensaje: " + ex);
            Log.escribirLog("Ha ocurrido un error al enviar el email: " + ex);
        }
    }

    
}
