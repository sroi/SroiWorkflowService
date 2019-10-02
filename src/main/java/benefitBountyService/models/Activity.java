package benefitBountyService.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "activity_capture")
public class Activity {

    @Id
    @Field("_id")
    private ObjectId activityId;
    private String projectId;
    private String taskId;
    private String userId;
    private String role;
    private String activity;
    private String comments;
    private String uploads;
    private String timeEntered;
    private String createdBy;
    private String updatedBy;
    private Date createdOn;
    private Date updatedOn;

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", projectId='" + projectId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                ", activity='" + activity + '\'' +
                ", comments='" + comments + '\'' +
                ", uploads='" + uploads + '\'' +
                ", timeEntered='" + timeEntered + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    public Activity(ObjectId activityId, String projectId, String taskId, String userId, String role, String activity, String comments, String uploads, String timeEntered, String createdBy, String updatedBy, Date createdOn, Date updatedOn) {
        this.activityId = activityId;
        this.projectId = projectId;
        this.taskId = taskId;
        this.userId = userId;
        this.role = role;
        this.activity = activity;
        this.comments = comments;
        this.uploads = uploads;
        this.timeEntered = timeEntered;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public String getActivityId() {
        return activityId.toString();
    }

    public void setActivityId(ObjectId activityId) {
        this.activityId = activityId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads;
    }

    public String getTimeEntered() {
        return timeEntered;
    }

    public void setTimeEntered(String timeEntered) {
        this.timeEntered = timeEntered;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}