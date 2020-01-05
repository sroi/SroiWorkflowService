package benefitBountyService.services;

import benefitBountyService.dao.FileRepository;
import benefitBountyService.dao.MailRepository;
import benefitBountyService.models.Mail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(FileDatabaseService.class);

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    public String sendEmailToUser(String emailId, String message, String subject) throws Exception {
        //Todo : Get User from session and set below.
        Mail mail = new Mail();
        String senderEmail = "tipsroi@gmail.com";
        mail.setEmailId(senderEmail);
        String password = "Sroi@1234";
        mail.setPassword(password);
        mail.setReceiver(emailId);
        mail.setMessage(message);
        mail.setSubject(subject);
        String result = mailRepository.sendEmail(mail);
        return result;
    }
}
