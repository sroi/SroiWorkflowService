package benefitBountyService.models;

import java.util.Date;
import benefitBountyService.user.IUser;
import benefitBountyService.user.IAdmin;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "projects")
public class Project {

	@Id
	private ObjectId projectId;
	private String name;
	private String areaOfEngagement;
	private String summary;
	private Date   startDate;
	private Date   endDate;
	private Double budget;
	@Field("corporate")
	private String associatedCorporateEntity;
	private String location;
	//private IUser  pointOfContact;
	//private IAdmin admin;

	public ObjectId getProjectId() {
		return projectId;
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

	public String getAssociatedCorporateEntity() {
		return associatedCorporateEntity;
	}

	public void setAssociatedCorporateEntity(String associatedCorporateEntity) {
		this.associatedCorporateEntity = associatedCorporateEntity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
