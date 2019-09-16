package benefitBountyService.controllers;

import benefitBountyService.models.Project;
import benefitBountyService.services.ProjectService;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
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
	
	@RequestMapping(value = "/getAll" , method=RequestMethod.GET)
	public List<Project> getProjects(){
        List<Project> projects = projectService.getProjects();
        return projects;
    }
	
	
//	@RequestMapping(value="/" , method=POST)
//	public void createProject(@RequestParam)

}
