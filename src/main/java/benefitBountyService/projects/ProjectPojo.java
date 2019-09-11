package benefitBountyService.projects;

import java.util.Date;
import benefitBountyService.user.IUser;
import benefitBountyService.user.IAdmin;

public class ProjectPojo {
	
	private String projectId;
	private String projectName;
	private String areaOfEngagement;
	private String projectSummary;
	private Date   projectStartDate;
	private Date   projectEndDate;
	private Double projectBudget;
	private String associatedCorporateEntity;
	private String projectLocation;
	private IUser  projectPointOfContact;
	private IAdmin projectAdmin;
	

}
