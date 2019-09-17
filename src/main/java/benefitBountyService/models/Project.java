package benefitBountyService.models;

import java.util.Date;
import java.util.List;

import benefitBountyService.user.IUser;
import benefitBountyService.user.IAdmin;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
	//@Field("corporate")
	private String corporate;
	private String location;
	private List<User> stakeholders;
	private List<User> pointOfContacts;
	//private IAdmin admin;

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

	public List<User> getStakeholders() {
		return stakeholders;
	}

	public void setStakeholders(List<User> stakeholders) {
		this.stakeholders = stakeholders;
	}

	public List<User> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<User> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
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
				", associatedCorporateEntity='" + corporate + '\'' +
				", location='" + location + '\'' +
				", stakeholders=" + stakeholders +
				", pointOfContacts=" + pointOfContacts +
				'}';
	}
}
