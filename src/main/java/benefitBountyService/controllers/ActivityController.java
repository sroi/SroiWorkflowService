package benefitBountyService.controllers;

import benefitBountyService.models.Activity;
import benefitBountyService.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/get")
    public List<Activity> getActivityData(@RequestParam("tid") String taskId, @RequestParam("pid") String projId) {
        return activityService.getActivityByTaskAndProject(taskId, projId);
    }
}
