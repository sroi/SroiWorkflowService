package benefitBountyService.models.dtos;

public class PTUserTO {

    private String id;
    private String name;
    private String email;
    private String phoneNo;

    public PTUserTO() {
        super();
    }

    public PTUserTO(String id, String name, String email, String phoneNo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
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

    @Override
    public String toString() {
        return "PTUserTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
