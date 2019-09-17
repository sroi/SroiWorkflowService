package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getProjects(){
        return projectRepository.findAll();
    }

    public List<Task> getTasksDetailsByName(String name) {
        List<Task> tasks = taskRepository.findByName(name);
        return tasks;
    }

    public List<Task> getTasksDetailsByProject(String projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks;
    }
}
