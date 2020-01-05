package benefitBountyService.dao;

import java.util.List;
import benefitBountyService.models.Mail;

public interface MailRepository {
    String sendEmail(Mail mail);
}
