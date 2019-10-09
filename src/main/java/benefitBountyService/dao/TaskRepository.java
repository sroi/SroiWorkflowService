package benefitBountyService.dao;

import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import java.util.List;

public interface TaskRepository {
    List<Task> findByProjectId(String projectId);

    Task findById(String taskId);

    long deleteById(String taskId);

    Task save(Task task);

    Task fetchByTaskId(String taskId);

    List<Task> getTasksAfterLogin(String userId, String role);

    int changeTaskStatus(User loggedInUser, Task task, String role, String status, String comments, String timeSpent);
}
