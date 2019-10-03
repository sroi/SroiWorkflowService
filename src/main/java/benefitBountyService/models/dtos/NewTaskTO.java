package benefitBountyService.models.dtos;

import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewTaskTO {

    @Field("_id")
    private String taskId;
    private String name;
    private String description;
    private String projectId;
    private String label;
    private Date startDate;
    private Date endDate;
    private String location;
    private List<UserTO> approver_info;
    private List<String> volunteers = new ArrayList<>();
    private String created_by;
    private Date created_on;
    private String updated_by;
    private Date updated_on;
    private List<NewProjectTO> proj_info;

    /*public NewTaskTO() {
        super();
    }

    public NewTaskTO(String taskId, String name, String description, String projectId, String activityLabel, Date startDate, Date endDate, String location, List<User> approver,
                     List<String> volunteers, String created_by, Date created_on, String updated_by, Date updated_on) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.activityLabel = activityLabel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.approver = approver;
        this.volunteers = volunteers;
        this.created_by = created_by;
        this.created_on = created_on;
        this.updated_by = updated_by;
        this.updated_on = updated_on;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getActivityLabel() {
        return activityLabel;
    }

    public void setActivityLabel(String activityLabel) {
        this.activityLabel = activityLabel;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<User> getApprover() {
        return approver;
    }

    public void setApprover(List<User> approver) {
        this.approver = approver;
    }

    public List<String> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<String> volunteers) {
        this.volunteers = volunteers;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }*/

    @Override
    public String toString() {
        return "NewTaskTO{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId='" + projectId + '\'' +
                ", label='" + label + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", approver_info='" + approver_info + '\'' +
                ", volunteers=" + volunteers +
                ", created_by='" + created_by + '\'' +
                ", created_on=" + created_on +
                ", updated_by='" + updated_by + '\'' +
                ", updated_on=" + updated_on +
                ", project=" + proj_info +
                '}';
    }
}
