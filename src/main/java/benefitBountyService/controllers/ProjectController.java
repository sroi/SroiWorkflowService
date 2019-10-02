package benefitBountyService.controllers;

import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.dtos.ProjectTO;
import benefitBountyService.services.ProjectService;
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

    /*
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
     */
	
	@RequestMapping(value = "/all" , method=RequestMethod.GET)
	public List<ProjectTO> getProjects(@RequestParam("user_id") String userId, @RequestParam("Role") String role) {
        List<ProjectTO> projects = projectService.getProjects(userId, role);
        return projects;
    }

    /**
     * Description - To find Project details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @RequestMapping(value = "/get" , method=RequestMethod.GET)
    public ProjectTO getProjectDetailsByProjectId(@RequestParam("pid") String projectId) {
        ProjectTO project = null;
        try {
            project = projectService.getProjectDetailsById(projectId);
            return project;
        } catch (ResourceNotFoundException e) {
//            System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide correct project Id", e);
        }
    }

    /**
     * Description - To create new projects.
     * Param - project object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public int addProject(@RequestBody ProjectTO project){
        return projectService.saveOrUpdate(project);
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
     * Description - To update project status.
     * Param - Project Id (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Task table.
     *                     -> 2 - failed- Task can not be updated. It is in <state> state.
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public int updateProjectStatus(@RequestParam("pid") String projectId, @RequestParam("status") String status){
        return projectService.updateProjectStatus(projectId, status);
    }

}
