package benefitBountyService.controllers;

import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import benefitBountyService.services.ProjectService;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/project")
public class ProjectController {


    @Autowired
    private ProjectService projectService;

    @RequestMapping("/ping")
    public String getProjectControllerStatus(){
        try {
            MongoClient mongoClient = new MongoClient("localhost");
            MongoDatabase database = mongoClient.getDatabase("sroi");
            //database.listCollectionNames();
            mongoClient.close();
            return "Your application is up and running.";
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return "Your application is down";
        }
    }
	
	@RequestMapping(value = "/all" , method=RequestMethod.GET)
	public List<Project> getProjects(){
        List<Project> projects = projectService.getProjects();
        return projects;
    }

    /**
     * Description - To find list of tasks satisfying given projectId condition.
     * Param - Project_id (in String format)
     * Return Value -  Return list of tasks
     */
    @RequestMapping(value = "/tasks" , method=RequestMethod.GET)
    public List<Task> getTasksDetailsByProject(@RequestParam("pr_id") String projectId){
        List<Task> tasks = projectService.getTasksDetailsByProject(projectId);
        return tasks;
    }

    /**
     * Description - To find list of tasks satisfying given task name condition.
     * Param - task_name (in String format)
     * Return Value -  Return list of tasks
     */
    @RequestMapping(value = "/tasks/name" , method=RequestMethod.GET)
    public List<Task> getTasksDetailsByName(@RequestParam("t_name") String taskName){
        List<Task> tasks = projectService.getTasksDetailsByName(taskName);
        return tasks;
    }
	
	
//	@RequestMapping(value="/" , method=POST)
//	public void createProject(@RequestParam)

}
