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
    private String proj_id;

    /*public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }*/

    public String getProj_id() {
        return proj_id;
    }

    public void setProj_id(String proj_id) {
        this.proj_id = proj_id;
    }

    public String getRowId() {
        return rowId != null ? rowId.toString() : null;
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
