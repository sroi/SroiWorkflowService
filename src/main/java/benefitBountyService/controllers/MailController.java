package benefitBountyService.controllers;


import benefitBountyService.models.Mail;
import benefitBountyService.services.MailService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping(value = "/send")
    public String sendEmail(@RequestParam("email_id") String emailId, @RequestParam("message") String message, @RequestParam("subject") String subject) throws Exception {
        String result = mailService.sendEmailToUser(emailId,message,subject);
        return result;
    }
}
