package benefitBountyService.models.sroi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class StepTwo {

    @Field("_id")
    private ObjectId rowId;
    private String stakeholder;
    private String goals;

    public String getRowId() {
        return rowId != null ? rowId.toString() : null;
    }

    public void setRowId(ObjectId rowId) {
        this.rowId = rowId;
    }

    public String getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(String stakeholder) {
        this.stakeholder = stakeholder;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }
}
