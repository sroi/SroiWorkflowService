package benefitBountyService.controllers;

import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import benefitBountyService.services.ProjectService;
import benefitBountyService.services.TaskService;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/ping")
    public String getProjectControllerStatus(){
        try {
            MongoClient mongoClient = new MongoClient("localhost");
            MongoDatabase database = mongoClient.getDatabase("sroi");
            //database.listCollectionNames();
            mongoClient.close();
            return "Your application is up and running.";
        } catch (MongoException | IllegalArgumentException ex) {
            ex.printStackTrace();
            return "Your application is down";
        }
    }
	
	@RequestMapping(value = "/all" , method=RequestMethod.GET)
	public List<Project> getProjects() {
        List<Project> projects = projectService.getProjects();
        return projects;
    }

    /**
     * Description - To find task details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @RequestMapping(value = "/get" , method=RequestMethod.GET)
    public Project getProjectDetailsByProjectId(@RequestParam("pid") String projectId) {
        Project project = null;
        try {
            project = projectService.getProjectDetailsById(projectId);
            return project;
        } catch (ResourceNotFoundException e) {
//            System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide correct project Id", e);
        }
    }

    /**
     * Description - To find list of tasks satisfying given projectId condition.
     * Param - Project_id (in String format)
     * Return Value -  Return list of tasks
     */
    @RequestMapping(value = "/tasks" , method=RequestMethod.GET)
    public List<Task> getTasksDetailsByProject(@RequestParam("pid") String projectId){
        List<Task> tasks = taskService.getTasksDetailsByProject(projectId);
        return tasks;
    }

    /**
     * Description - To find task details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @RequestMapping(value = "/task" , method=RequestMethod.GET)
    public Task getTasksDetailsByTaskId(@RequestParam("tid") String taskId) {
        Task task = null;
        try {
            task = taskService.getTaskDetailsById(taskId);
            return task;
        } catch (ResourceNotFoundException e) {
//            System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide correct task Id", e);
        }
    }

    /**
     * Description - To find list of tasks satisfying given task name.
     * Param - task_name (in String format)
     * Return Value -  Return list of tasks
     */
    @RequestMapping(value = "/tasks/name" , method=RequestMethod.GET)
    public List<Task> getTasksDetailsByName(@RequestParam("tname") String taskName){
        List<Task> tasks = taskService.getTasksDetailsByName(taskName);
        return tasks;
    }

    /**
     * Description - To create new projects.
     * Param - project object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public int addProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    /**
     * Description - To create new task.
     * Param - task object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    public int addTask(@RequestBody Task task){
        return taskService.createTask(task);
    }

    /**
     * Description - To delete existing project.
     * Param - Project_id (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Project table.
     *                     -> 2 - failed- Project can not be deleted. It is in <state> state.
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public int deleteProject(@RequestParam("pid") String projectId){
        return projectService.deleteProject(projectId);
    }

    /**
     * Description - To delete existing task.
     * Param - Task (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Task table.
     *                     -> 2 - failed- Task can not be deleted. It is in <state> state.
     */
    @RequestMapping(value = "/deleteTask", method = RequestMethod.DELETE)
    public int deleteTask(@RequestParam("tid") String taskId){
        return taskService.deleteTask(taskId);
    }

    /**
     * Description - To update project status.
     * Param - Project Id (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Task table.
     *                     -> 2 - failed- Task can not be deleted. It is in <state> state.
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public boolean updateProjectStatus(@RequestParam("pid") String projectId, @RequestParam("status") String status){
        return projectService.updateProjectStatus(projectId, status);
    }

}
