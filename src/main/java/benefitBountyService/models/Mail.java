package benefitBountyService.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

public class Mail {

    @Field("email_id")
    @BsonProperty("email_id")
    private String emailId;

    @Field("password")
    @BsonProperty("password")
    private String password;

    @Field("receiver")
    @BsonProperty("receiver")
    private String receiver;

    @Field("message")
    @BsonProperty("message")
    private String message;

    @Field("subject")
    @BsonProperty("subject")
    private String subject;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
