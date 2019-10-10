package benefitBountyService.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "tasks")
//@BsonDiscriminator
public class Task {

    @Id
    @Field("_id")
//    @BsonId
    private ObjectId taskId;
    private String name;
    private String description;
    private ObjectId projectId;

    @Field("label")
    @JsonSetter("activityLabel")
//    @BsonProperty("label")
    private String activityLabel;

    private Date startDate;
    private Date endDate;
    private String location;

    @JsonIgnore
    private ObjectId approver;

    @JsonIgnore
    private List<ObjectId> volunteers = new ArrayList<>();

    private String status;

    @BsonIgnore
    private Double totalTimeSpent;
    private String created_by;
    private Date created_on;
    private String updated_by;
    private Date updated_on;

    @BsonIgnore
    private User approver_info;

    @BsonIgnore
    @Transient
    private List<User> vols_info = new ArrayList<>();

    @BsonIgnore
    @JsonIgnore
    private User volunteer_info;

    @BsonIgnore
    private Project project_info;

    //Todo : to fix issue while serializing. @Transient is not working here as while displaying result to ui it removes activity_info field
    @BsonIgnore
    private List<Activity> activity_info = new ArrayList<>();

    public Task(){
        super();
    }

//    @BsonCreator
    public Task(ObjectId taskId, String name, String description, ObjectId projectId, String activityLabel, Date startDate, Date endDate, String location,
                ObjectId approver, List<ObjectId> volunteers, String status, String created_by, Date created_on, String updated_by, Date updated_on) {
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
        this.status = status;
        this.created_by = created_by;
        this.created_on = created_on;
        this.updated_by = updated_by;
        this.updated_on = updated_on;
    }

    public String getTaskId() {
        return taskId != null ? taskId.toString() : null;
    }

    public void setTaskId(ObjectId taskId) {
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
        return projectId != null ? projectId.toString() : null;
    }

    public void setProjectId(ObjectId projectId) {
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

    public String getApprover() {
        return approver != null ? approver.toString() : null;
    }

    public void setApprover(ObjectId approver) {
        this.approver = approver;
    }

    public List<ObjectId> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<ObjectId> volunteers) {
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
    }

    public User getVolunteer_info() {
        return volunteer_info;
    }

    public void setVolunteer_info(User volunteer_info) {
        this.volunteer_info = volunteer_info;
    }

    public List<User> getVols_info() {
        return vols_info;
    }

    public void setVols_info(List<User> vols_info) {
        this.vols_info = vols_info;
    }

    public User getApprover_info() {
        return approver_info;
    }

    public void setApprover_info(User approver_info) {
        this.approver_info = approver_info;
    }

    public Project getProject_info() {
        return project_info;
    }

    public void setProject_info(Project project_info) {
        this.project_info = project_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Activity> getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(List<Activity> activity_info) {
        this.activity_info = activity_info;
    }

    public Double getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(Double totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    @JsonGetter("duration")
    public String duration() {
        String duration = String.valueOf(((endDate.getTime() - startDate.getTime())/3600000));
        return duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId='" + projectId + '\'' +
                ", activityLabel='" + activityLabel + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", approver=" + approver +
                ", volunteers=" + volunteers +
                ", created_by='" + created_by + '\'' +
                ", created_on=" + created_on +
                ", updated_by='" + updated_by + '\'' +
                ", updated_on=" + updated_on +
                ", approver_info=" + approver_info +
                ", volunteers_info=" + volunteer_info +
                ", project_info=" + project_info +
                '}';
    }
}
