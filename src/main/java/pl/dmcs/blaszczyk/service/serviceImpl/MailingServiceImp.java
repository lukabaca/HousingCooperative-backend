package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ServerException;
import pl.dmcs.blaszczyk.service.MailingService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@PropertySource("classpath:mail.properties")
@Service
public class MailingServiceImp implements MailingService {

    @Autowired
    private Environment env;

    @Override
    public void sendMail(String to, String title, String messageContent) {
        try {
            Session session = createSession();
            MimeMessage message = new MimeMessage(session);
            prepareEmailMessage(message, to, title, messageContent);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ServerException("Error in sending mail");
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", env.getRequiredProperty("mail.smtp.auth"));//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", env.getRequiredProperty("mail.smtp.starttls.enable"));//TLS must be activated
        props.put("mail.smtp.host", env.getRequiredProperty("mail.smtp.host")); //Outgoing server (SMTP) - change it to your SMTP server
        props.put("mail.smtp.starttls.required", env.getRequiredProperty("mail.smtp.starttls.required"));
        props.put("mail.smtp.port", env.getRequiredProperty("mail.smtp.port"));//Outgoing port
        props.put("mail.transport.protocol", env.getRequiredProperty("mail.transport.protocol"));
        props.put("mail.smtp.ssl.enable", env.getRequiredProperty("mail.smtp.ssl.enable"));
        return props;
    }

    private Session createSession() {
        return Session.getInstance(this.getProperties(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( env.getRequiredProperty("senderMail"),  env.getRequiredProperty("senderPassword"));
            }
        });
    }

    private void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException {
        message.setContent(html, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(env.getRequiredProperty("senderMail")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }
}
