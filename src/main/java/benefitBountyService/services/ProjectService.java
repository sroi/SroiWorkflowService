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
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getProjects(){
//        Todo : get projects depending upon roles
        return projectRepository.findAll();
    }

    public boolean getProjectById(String projectId){
        boolean found = false;
        Optional<Project> prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl.isPresent()){
            System.out.println("Below are project details: "+prjOpnl.get());
            found = true;
        }
        /*prjOpnl.ifPresent( prj -> {
            System.out.println("Value found: "+ prj);
            found = true;
        });*/
        return found;
    }

    public boolean getTaskById(String taskId){
        boolean found = false;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            System.out.println("Below are Task details: "+tskOpnl.get());
            found = true;
        }
        return found;
    }

    public List<Task> getTasksDetailsByName(String name) {
        List<Task> tasks = taskRepository.findByName(name);
        return tasks;
    }

    public List<Task> getTasksDetailsByProject(String projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks;
    }

    public int deleteProject(String projectId) {
        boolean foundProject = getProjectById(projectId);
//        Todo: create ProjectNotFoundException
//        Todo: logic to check if project is in valid state to delete and set foundProject flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);
        if(foundProject){
            System.out.println("Project found");
            projectRepository.deleteById(projectId);
            return 0; // successful deletion
        } else {
            System.out.println("Project not found. Please refresh Project table");
            return 1; // failed- Project not found. Please refresh Project table.
        }
        //return 2; failed- Project can not be deleted. It is in <state> state.
    }

    public int deleteTask(String taskId) {
        boolean foundTask = getTaskById(taskId);
//        Todo: create TaskNotFoundException
//        Todo: logic to check if task is in valid state to delete and set foundTask flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);
        if(foundTask){
            System.out.println("Task found");
            taskRepository.deleteById(taskId);
            return 0; // successful deletion
        } else {
            System.out.println("Task not found. Please refresh Project table");
            return 1; // failed- Project not found. Please refresh Project table.
        }
        //return 2; failed- Project can not be deleted. It is in <state> state.
    }

    public int createProject(Project project) {
        int returnVal = 1;
        project.setProjectId(ObjectId.get());
        System.out.println("Following object has been obtained: "+project);
        System.out.println("************************");
        Project proj = projectRepository.save(project);
        System.out.println("Following project has been saved successfully: \\n"+proj);
        //return project;
        if (proj != null)
            returnVal = 0;
        return returnVal;
    }

    public int createTask(Task task) {
        int returnVal = 1;
        task.setTaskId(ObjectId.get());
        System.out.println("Following object has been obtained: "+task);
        System.out.println("************************");
        Task savedTask = taskRepository.save(task);
        System.out.println("Following project has been saved successfully: \\n"+savedTask);
        //return project;
        if (savedTask != null)
            returnVal = 0;
        return returnVal;
    }


}
