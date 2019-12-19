package benefitBountyService.dao.impl;

import benefitBountyService.dao.MailRepository;
import benefitBountyService.models.Mail;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

public class MailRepositoryImpl implements MailRepository{
    @Override
    public void sendEmail(Mail mail) {

        String to = mail.getReceiver();

        // Sender's email ID needs to be mentioned
        String from = mail.getEmailId();

        // Assuming you are sending email from localhost
        String host = "gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(mail.getMessage());

            // Now set the actual message
            message.setText(mail.getMessage());

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
