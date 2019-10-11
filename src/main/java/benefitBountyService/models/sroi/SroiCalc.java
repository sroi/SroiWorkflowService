package benefitBountyService.models.sroi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

public class SroiCalc {
    @Field("proj_id")
    private ObjectId projectId;

    @Field("user_id")
    private ObjectId userId;
    private StepOne stepOne;
    private List<StepTwo> stepTwo;
    //audit fields
    private String created_by;
    private Date created_on;
    private String updated_by;
    private Date updated_on;

    public String getProjectId() {
        return projectId != null ? projectId.toString() : null;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId != null ? userId.toString() : null;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public StepOne getStepOne() {
        return stepOne;
    }

    public void setStepOne(StepOne stepOne) {
        this.stepOne = stepOne;
    }

    public List<StepTwo> getStepTwo() {
        return stepTwo;
    }

    public void setStepTwo(List<StepTwo> stepTwos) {
        this.stepTwo = stepTwos;
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
}
