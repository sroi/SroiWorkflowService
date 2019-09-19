package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Project;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjects(){
//        Todo : get projects depending upon roles
        List<Project> projects = null;
        try {
            projects = projectRepository.findAll();
            if (projects.isEmpty())
                logger.info("collection 'projects' is empty. Please load some data through 'Create Project' page");
        } catch (MongoException ex) {
            logger.error("Failure while loading projects in Mongo. {}");
        }
        return projects;
    }

    public boolean checkProjectById(String projectId){
        boolean found = false;
        Optional<Project> prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl.isPresent()){
            logger.info("Below are project details: "+prjOpnl.get());
            found = true;
        }
        /*prjOpnl.ifPresent( prj -> {
            System.out.println("Value found: "+ prj);
            found = true;
        });*/
        return found;
    }

    public Project getProjectDetailsById(String projectId) throws ResourceNotFoundException {
        Project project = null;
        Optional<Project> prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl.isPresent()){
            project = prjOpnl.get();
            logger.info("Below are Project details: "+ project);
        } else {
            logger.warn("Project with id '" + projectId + "' is not present.");
            throw new ResourceNotFoundException();
        }
        return project;
    }

    public int deleteProject(String projectId) {
        boolean foundProject = checkProjectById(projectId);
//        Todo: create ProjectNotFoundException
//        Todo: logic to check if project is in valid state to delete and set foundProject flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);
        if(foundProject){
            logger.info("Delting project with id '" + projectId + "'.");
            projectRepository.deleteById(projectId);
            return 0; // successful deletion
        } else {
            logger.info("Project with id '"+ projectId + "'not found.");
            return 1; // failed- Project not found. Please refresh Project table.
        }
        //return 2; failed- Project can not be deleted. It is in <state> state.
    }

    public int createProject(Project project) {
        int returnVal = 1;
        project.setProjectId(ObjectId.get());
        logger.info("Following project has been recieved from User: "+project);
        Project proj = projectRepository.save(project);
        logger.info("Following project has been saved successfully: \\n"+proj);
        //return project;
        if (proj != null)
            returnVal = 0;
        return returnVal;
    }

    public boolean updateProjectStatus(String projectId, String status) {
        boolean updated = false;
        try {
            Project project = getProjectDetailsById(projectId);
            logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
            project.setStatus(status);
            projectRepository.save(project);
            updated = true;
        } catch (ResourceNotFoundException e) {
            String errMsg = "Project not found with id '" + projectId+"'.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg, e);
        }
        return updated;
    }
}
