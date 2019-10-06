package benefitBountyService.models;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("activity_capture")
public class Activity {

    @Id
    @Field
    private ObjectId _id;
    private ObjectId projectId;
    private ObjectId taskId;
    private ObjectId userId;
    private String role;
    private String activity;
    private String comments;
//    private
    private Double timeEntered;
    private String status;
    private String createdBy;
    private String updatedBy;
    private Date createdOn;
    private Date updatedOn;

    public Activity() {
        super();
    }

    public Activity(ObjectId _id, ObjectId projectId, ObjectId taskId, ObjectId userId, String role, String comments, Double timeEntered,
                    String status, String createdBy, Date createdOn, String updatedBy, Date updatedOn) {
        this._id = _id;
        this.projectId = projectId;
        this.taskId = taskId;
        this.userId = userId;
        this.role = role;
        this.comments = comments;
        this.timeEntered = timeEntered;
        this.status = status;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public Activity(ObjectId _id, ObjectId projectId, ObjectId taskId, ObjectId userId, String role, String comments, String createdBy, Date createdOn,
                    String updatedBy, Date updatedOn) {
        this._id = _id;
        this.projectId = projectId;
        this.taskId = taskId;
        this.userId = userId;
        this.role = role;
        this.comments = comments;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public String get_id() {
        return _id.toString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getProjectId() {
        return projectId.toString();
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId.toString();
    }

    public void setTaskId(ObjectId taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId.toString();
    }

    public void setUserId(ObjectId userId) {
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

    public Double getTimeEntered() {
        return timeEntered;
    }

    public void setTimeEntered(Double timeEntered) {
        this.timeEntered = timeEntered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Activity{" +
                "_id=" + _id +
                ", projectId=" + projectId +
                ", taskId=" + taskId +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                ", activity='" + activity + '\'' +
                ", comments='" + comments + '\'' +
                ", timeEntered='" + timeEntered + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
