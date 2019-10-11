package benefitBountyService.models.sroi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class SroiCalc {
    @Field("proj_id")
    private ObjectId projectId;

    @Field("user_id")
    private ObjectId userId;
    private StepOne stepOne;
    private List<StepTwo> stepTwos;

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

    public List<StepTwo> getStepTwos() {
        return stepTwos;
    }

    public void setStepTwos(List<StepTwo> stepTwos) {
        this.stepTwos = stepTwos;
    }
}
