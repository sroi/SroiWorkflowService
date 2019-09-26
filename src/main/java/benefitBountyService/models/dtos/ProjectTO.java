package benefitBountyService.models.dtos;

import java.util.Date;

public class ProjectTO {

    private String projectId;
    private String name;
    private String areaOfEngagement;
    private String summary;
    private Date startDate;
    private Date endDate;
    private Double budget;
    private String corporate;
    private String location;
    private UserTO stakeholder;
    private UserTO pointOfContact;
    private String status;

    public ProjectTO(String projectId, String name, String areaOfEngagement, String summary, Date startDate, Date endDate, Double budget, String corporate, String location,
                     UserTO stakeholder, UserTO pointOfContact, String status) {
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

    public UserTO getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(UserTO stakeholder) {
        this.stakeholder = stakeholder;
    }

    public UserTO getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(UserTO pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
                ", stakeholder=" + stakeholder +
                ", pointOfContacts=" + pointOfContact +
                ", status='" + status + '\'' +
                '}';
    }
}
