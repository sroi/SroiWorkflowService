package benefitBountyService.services;

import benefitBountyService.dao.FileRepository;
import benefitBountyService.dao.MailRepository;
import benefitBountyService.models.Mail;
import benefitBountyService.models.Mail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(FileDatabaseService.class);

    @Autowired
    private MailRepository mailRepository;

    public String sendEmailToUser(@RequestParam("email_id") String emailId, @RequestParam("message") String message, @RequestParam String subject) throws Exception {
        //Todo : Get User from session and set below.
        Mail mail = new Mail();
        String senderEmail = "tipsroi@gmail.com";
        mail.setEmailId(senderEmail);
        String password = "Sroi#1234";
        mail.setPassword(password);
        mail.setReceiver(emailId);
        mail.setMessage(message);
        mail.setSubject(subject);
        mailRepository.sendEmail(mail);
        return "Done";
    }
}
