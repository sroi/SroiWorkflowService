package benefitBountyService.dao;

import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findByProjectId(String projectId);
    List<Task> findByName(String name);

    Task findById(String taskId);

    long deleteById(String taskId);

    Task save(Task task);
    // List<Task> findByTaskName(String name);
    Task fetchByTaskId(String taskId);

    void fetchByUserIdT(String taskId);

    List<Task> getTasksAfterLogin(String userId, String role);

    int changeTaskStatus(User loggedInUser, Task task, String role, String status, String comments, String timeSpent);
}
