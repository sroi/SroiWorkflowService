package benefitBountyService.models.dtos;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class NewProjectTO {


    @Field("_id")
    private String projectId;
    private String name;
    private String areaOfEngagement;
    private String summary;
    private Date startDate;
    private Date endDate;
    private Double budget;
    private String corporate;
    private String location;
    private String stakeholder;
    private String pointOfContact;
    private String status;
    private Double rating;

    @Field("created_on")
    private Date createdOn;

    @Field("created_by")
    private String createdBy;

    @Field("updated_on")
    private Date updatedOn;

    @Field("updated_by")
    private String updatedBy;

    /*public NewProjectTO() {
        super();
    }

    public NewProjectTO(String projectId, String name, String areaOfEngagement, String summary, Date startDate, Date endDate, Double budget, String corporate, String location,
                        PTUserTO stakeholder, PTUserTO pointOfContact, String status, Double rating, String updatedBy, Date updatedOn, String createdBy, Date createdOn) {
        this.projectId = projectId;
        this.name = name;
        this.areaOfEngagement = areaOfEngagement;
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.corporate = corporate;
        this.location = location;
        this.stakeholder = stakeholder;
        this.pointOfContact = pointOfContact;
        this.status = status;
        this.rating = rating;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaOfEngagement() {
        return areaOfEngagement;
    }

    public void setAreaOfEngagement(String areaOfEngagement) {
        this.areaOfEngagement = areaOfEngagement;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getCorporate() {
        return corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PTUserTO getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(PTUserTO stakeholder) {
        this.stakeholder = stakeholder;
    }

    public PTUserTO getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(PTUserTO pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
*/
    @Override
    public String toString() {
        return "ProjectTO{" +
                "projectId='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", areaOfEngagement='" + areaOfEngagement + '\'' +
                ", summary='" + summary + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", budget=" + budget +
                ", corporate='" + corporate + '\'' +
                ", location='" + location + '\'' +
//                ", stakeholder=" + stakeholder +
//                ", pointOfContacts=" + pointOfContact +
                ", status='" + status + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
