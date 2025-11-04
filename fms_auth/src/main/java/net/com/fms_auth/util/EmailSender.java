/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.util;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
public class EmailSender {
    public static boolean sendMail(String subject, String message, String receiverEmail) throws Exception {
        Transport transport = null;
        boolean rc = false;
        String senderEmail = "infor@dlb.lk";
        try {
            String messageText = message;
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.socketFactory.port", "587");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");
            Session mailSession = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(senderEmail));
            InternetAddress[] address = { new InternetAddress(receiverEmail) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = messageText;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            transport = mailSession.getTransport("smtps");
            transport.connect("smtp.gmail.com", "pjayasekaraw@gmail.com", "waiaowpgzmlpdlna");
            transport.sendMessage(msg, msg.getAllRecipients());
            rc = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
        return rc;
    }
}
