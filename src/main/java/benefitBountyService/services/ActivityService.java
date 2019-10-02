package benefitBountyService.services;

import benefitBountyService.dao.ActivityRepository;
import benefitBountyService.models.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);


    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> getActivityByTask(String taskId) throws Exception {
        Activity activity = null;
        System.out.println ("Below are taskid details  getActivityByTaskId: \n"+ taskId );
        List<Activity> activityList = activityRepository.findByTaskId(taskId);
        System.out.println (activityList.isEmpty() );
        logger.info("Below are taskid details  getActivityByTaskId: \n"+ taskId );
        if (!activityList.isEmpty()){

            logger.info("Below are taskid details getActivityByTaskId: \n"+ activityList );
        } else {

            logger.warn("taskid with id '" + taskId + "' is not present.");
            throw new Exception();
        }
        return activityList;
    }

    public List<Activity> getActivityByTaskAndProject(String taskId, String projId) {
        List<Activity> activities = activityRepository.findByTaskIdAndProjectId(taskId, projId);
        return activities;
    }
}
