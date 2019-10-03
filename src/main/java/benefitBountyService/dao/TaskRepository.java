package benefitBountyService.dao;

import benefitBountyService.models.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findByProjectId(String projectId);
    List<Task> findByName(String name);

    Task findById(String taskId);

    void deleteById(String taskId);

    Task save(Task task);
    // List<Task> findByTaskName(String name);
    Task fetchByUserId(String taskId);

    void fetchByUserIdT(String taskId);
}
