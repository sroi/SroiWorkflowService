package benefitBountyService.models.dtos;

import benefitBountyService.models.User;

import java.util.List;

public class TempTO {
    private String name;
    private List<UserTO>  approver_info;
//    private List<UserTO> approver_details;

    @Override
    public String toString() {
        return "TempTO{" +
                "name='" + name + '\'' +
                ", approver='" + approver_info + '\'' +
//                ", approver_details=" + approver +
                '}';
    }
}
