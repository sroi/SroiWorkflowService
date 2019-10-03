package benefitBountyService.dao.impl;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Task;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public List<Task> findByProjectId(String projectId) {
        return null;
    }

    @Override
    public List<Task> findByName(String name) {
        return null;
    }

    @Override
    public Task findById(String taskId) {
        return null;
    }

    @Override
    public void deleteById(String taskId) {

    }

    @Override
    public Task save(Task task) {
        return null;
    }
}
