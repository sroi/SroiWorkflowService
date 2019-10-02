package benefitBountyService.dao;

import benefitBountyService.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByProjectId(String projectId);
    List<Task> findByName(String name);
   // List<Task> findByTaskName(String name);
    List<Task> findByTaskIdAndProjectId(String taskId, String projectId);

}
