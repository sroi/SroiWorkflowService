package benefitBountyService.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {

    @Id
    private Object _id;
    @Indexed(unique = true)
    @Field("user_id")
    private String userId;
    private String password;
    private String name;
    private String email;
    private String details;
    @Field("phone_no")
    private String phoneNo;
    @Field("is_admin")
    private String admin;
    @Field("is_stakeholder")
    private String stakeholder;
    @Field("is_approver")
    private String approver;
    @Field("is_volunteer")
    private String volunteer;

    private static String YES = "Y";

    public String get_id() {
        return _id.toString();
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return password;
    }

    public void setPwd(String pwd) {
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
                isValid = getAdmin().equals(YES); break;
            case Approver:
                isValid = getApprover().equals(YES); break;
            case Stakeholder:
                isValid = getStakeholder().equals(YES); break;
            case Volunteer:
                isValid = getVolunteer().equals(YES); break;
            default:
                isValid = false;
        }
        return isValid;
    }
}
