package benefitBountyService.dao;

import benefitBountyService.models.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByProjectId(String projectId);
    List<Task> findByName(String name);
   // List<Task> findByTaskName(String name);
}
