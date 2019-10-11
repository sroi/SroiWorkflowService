package benefitBountyService.models.sroi;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class StepOne {

    @Field("_id")
    private ObjectId rowId;
    private String scope;
    private String workplan;
    private String resource;
    private String timeline;

    /*public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }*/

    public ObjectId getRowId() {
        return rowId;
    }

    public void setRowId(ObjectId rowId) {
        this.rowId = rowId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getWorkplan() {
        return workplan;
    }

    public void setWorkplan(String workplan) {
        this.workplan = workplan;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resources) {
        this.resource = resources;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
}
