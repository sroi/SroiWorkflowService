package benefitBountyService.models;

import benefitBountyService.utils.Constants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.bson.codecs.pojo.annotations.*;

//@Document(collection = "users")
public class User {

    @BsonId
    private ObjectId _id;
//    @Indexed(unique = true)
    @BsonProperty("user_id")
    private String userId;
    private String password;
    private String name;
    private String email;
    private String details;
//    @Field("phone_no")
    @BsonProperty("phone_no")
    private String phoneNo;
    @BsonProperty("is_admin")
    private String admin = Constants.NO;
    @BsonProperty("is_stakeholder")
    private String stakeholder = Constants.NO;
    @BsonProperty("is_approver")
    private String approver = Constants.NO;
    @BsonProperty("is_volunteer")
    private String volunteer = Constants.NO;

    public String get_id() {
        return _id.toString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return email;
    }

    public void setEmailId(String emailId) {
        this.email = emailId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(String stakeholder) {
        this.stakeholder = stakeholder;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", details='" + details + '\'' +
                ", phoneNo=" + phoneNo +
                ", admin=" + admin +
                ", stakeholder=" + stakeholder +
                ", approver=" + approver +
                ", volunteer=" + volunteer +
//                ", status=" + status +
                '}';
    }

    public enum Roles {
        Admin("Admin"), Approver("Approver"), Stakeholder("Stakeholder"),  Volunteer("Volunteer");
        private String val;
        Roles(String val){
            this.val = val;
        }
    }

    public boolean isValidRole(String role) {
        boolean isValid = false;
        switch(Roles.valueOf(role)){
            case Admin:
                isValid = getAdmin().equals(Constants.YES); break;
            case Approver:
                isValid = getApprover().equals(Constants.YES); break;
            case Stakeholder:
                isValid = getStakeholder().equals(Constants.YES); break;
            case Volunteer:
                isValid = getVolunteer().equals(Constants.YES); break;
            default:
                isValid = false;
        }
        return isValid;
    }
}
