package benefitBountyService.models.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;

public class PTUserTO {

    private String _id;
    private String name;
    private String email;
    private String phoneNo;

    public PTUserTO() {
        super();
    }

    public PTUserTO(String id, String name, String email, String phoneNo) {
        this._id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    @JsonGetter("_id")
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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
