package benefitBountyService.dao;

import benefitBountyService.models.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity,String> {

    List<Activity> findByTaskId(String taskId);
//    List<Activity> findByActivityId(String task);
}
