package benefitBountyService.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "projects")
public class Project {

	@Id
	@Field("_id")
	private ObjectId projectId;
	private String name;
	private String areaOfEngagement;
	private String summary;
	private Date startDate;
	private Date endDate;
	private Double budget;
	@Field("corporate")
	@JsonSetter("corporate")
	private String associatedCorporateEntity;
	private String location;
	private String stakeholder;
	private String pointOfContacts;
	private String status;
	//private IAdmin admin;


	public Project(ObjectId projectId, String name, String areaOfEngagement, String summary, Date startDate, Date endDate, Double budget, String associatedCorporateEntity,
				   String location, String stakeholder, String pointOfContacts, String status) {
		this.projectId = projectId;
		this.name = name;
		this.areaOfEngagement = areaOfEngagement;
		this.summary = summary;
		this.startDate = startDate;
		this.endDate = endDate;
		this.budget = budget;
		this.associatedCorporateEntity = associatedCorporateEntity;
		this.location = location;
		this.stakeholder = stakeholder;
		this.pointOfContacts = pointOfContacts;
		this.status = status;
	}

	public String getProjectId() {
		return projectId.toString();
	}

	public void setProjectId(ObjectId projectId) {
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
		return associatedCorporateEntity;
	}

	public void setCorporate(String corporate) {
		this.associatedCorporateEntity = corporate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStakeholder() {
		return stakeholder;
	}

	public void setStakeholder(String stakeholder) {
		this.stakeholder = stakeholder;
	}

	public String getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(String pointOfContact) {
		this.pointOfContacts = pointOfContact;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Project{" +
				"projectId=" + projectId +
				", name='" + name + '\'' +
				", areaOfEngagement='" + areaOfEngagement + '\'' +
				", summary='" + summary + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", budget=" + budget +
				", associatedCorporateEntity='" + associatedCorporateEntity + '\'' +
				", location='" + location + '\'' +
				", stakeholders=" + stakeholder +
				", pointOfContacts=" + pointOfContacts +
				", status='" + status + '\'' +
				'}';
	}
}
