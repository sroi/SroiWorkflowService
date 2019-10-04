package benefitBountyService.models.dtos;

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
    private List<UserTO> volunteers = new ArrayList<>();
    private String created_by;
    private Date created_on;
    private String updated_by;
    private Date updated_on;
    private List<NewProjectTO> proj_info;

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
