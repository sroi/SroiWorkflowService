package benefitBountyService.models.dtos;

public class UserTO {

    private String id;
    private String userId;
    private String name;
    private String email;
    private String phoneNo;
    private String details;
    private String admin;
    private String stakeholder;
    private String approver;
    private String volunteer;

    public UserTO() {
        super();
    }

    public UserTO(String id, String userId, String name, String email, String phoneNo, String details, String admin, String stakeholder, String approver, String volunteer) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.details = details;
        this.admin = admin;
        this.stakeholder = stakeholder;
        this.approver = approver;
        this.volunteer = volunteer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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
        return "UserTO{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", details='" + details + '\'' +
                ", admin='" + admin + '\'' +
                ", stakeholder='" + stakeholder + '\'' +
                ", approver='" + approver + '\'' +
                ", volunteer='" + volunteer + '\'' +
                '}';
    }
}
