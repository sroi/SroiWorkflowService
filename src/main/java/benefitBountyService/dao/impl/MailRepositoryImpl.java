package benefitBountyService.dao.impl;

import benefitBountyService.dao.MailRepository;
import benefitBountyService.models.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

public class MailRepositoryImpl implements MailRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String sendEmail(Mail mail) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("tipsroi@gmail.com","Sroi@1234");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("tipsroi@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail.getReceiver()));
            String ccAddress = "bachkarakash@gmail.com";
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(ccAddress));
            message.setSubject(mail.getSubject());
            message.setText(mail.getMessage());
            Transport.send(message);
        }catch (MessagingException mex) {
            mex.printStackTrace();
            return "Error";
        }
        return "Done";

    }
}
