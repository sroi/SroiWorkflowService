package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.exceptions.SroiResourceNotFoundException;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.ProjectTO;
import benefitBountyService.models.dtos.UserTO;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    public List<ProjectTO> getProjects(){
//        Todo : get projects depending upon roles
        List<Project> projects = null;
        List<ProjectTO> projectTOs = new ArrayList<>();
        List<User> users = null;
        logger.info("Retrieving projects...");
        try {
            projects = projectRepository.findAll();
            users = userService.getUsers();
            if (users.isEmpty()) {
                logger.info("collection 'users' is empty.");
            }
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
            if (projects.isEmpty())
                logger.info("collection 'projects' is empty. Please load some data through 'Create Project' page");
//            projectTOs = projects.stream().map(prj -> {
            for (Project prj : projects) {
                UserTO stHolder = null;
                if (!prj.getStakeholder().equals(null)) {
                    User sh = userMap.get(prj.getStakeholder());
                    stHolder = new UserTO(sh.get_id(), sh.getName(), sh.getEmailId(), sh.getPhoneNo());
                }

//                List<UserTO> pocs = new ArrayList<>();
//                if (!prj.getPointOfContacts().isEmpty()) {
                    //for (String poc : prj.getPointOfContacts()) {
                        User pocUser = userMap.get(prj.getPointOfContacts());
                        UserTO pocTo = new UserTO(pocUser.get_id(), pocUser.getName(), pocUser.getEmailId(), pocUser.getPhoneNo());
//                    }
//                }
                ProjectTO projectTO = new ProjectTO(prj.getProjectId(), prj.getName(), prj.getAreaOfEngagement(), prj.getSummary(), prj.getStartDate(), prj
                        .getEndDate(), prj.getBudget(), prj.getCorporate(), prj.getLocation(), stHolder, pocTo, prj.getStatus());
                projectTOs.add(projectTO);
            }
//            }).collect(Collectors.toList());
        } catch (MongoException ex) {
            logger.error("Failure while loading projects in Mongo. {}");
        }
        logger.info("Below are projects details..." + projectTOs);
        return projectTOs;
    }

    public boolean checkProjectById(String projectId){
        boolean found = false;
        Optional<Project> prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl.isPresent()){
            logger.info("Project found. Below are project details: "+prjOpnl.get());
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
            logger.info("Deleting project with id '" + projectId + "'.");
            projectRepository.deleteById(projectId);
            return 0; // successful deletion
        } else {
            String errMsg = "Project with id '"+ projectId + "' is not found.";
            //logger.info(errMsg);
            throw new SroiResourceNotFoundException(errMsg);
            //return 1; // failed- Project not found. Please refresh Project table.
        }
        //return 2; failed- Project can not be deleted. It is in <state> state.
    }

    public int saveOrUpdate(ProjectTO prjTO) {
        int returnVal = 1;
        logger.info("Following project details have been received from User: "+prjTO);
        // Todo : To remove checkProjectById(project.getProjectId() from below
        // Todo: Logic for which fields to allow to be updated
        // Todo: Need to provide transactional support.
        Project prj = null;
        Optional<Project> prjOpnl = projectRepository.findById(prjTO.getProjectId());
        if (prjTO.getProjectId() != null && prjOpnl.isPresent()) {
            logger.info("Updating existing project.");
            prj = new Project(new ObjectId(prjTO.getProjectId()), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), prjTO.getStakeholder().getUserId(), prjTO.getPointOfContact().getUserId(), prjTO.getStatus());
        } else {
            prj = new Project(new ObjectId(prjTO.getProjectId()), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), prjTO.getStakeholder().getUserId(), prjTO.getPointOfContact().getUserId(), "Created");
            prj.setProjectId(ObjectId.get());
            logger.info("New Project will be created.");
        }
        Project savedProj = projectRepository.save(prj);
        logger.info("Following project has been saved successfully: \n"+savedProj);
        //return project;
        if (savedProj != null)
            returnVal = 0;
        return returnVal;
    }

    public int updateProjectStatus(String projectId, String status) {
        int updated = 1;
        try {
            Project project = getProjectDetailsById(projectId);
            logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
            project.setStatus(status);
            projectRepository.save(project);
            updated = 0;
        } catch (ResourceNotFoundException e) {
            String errMsg = "Project with id '" + projectId+"' not found.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg, e);
        }
        return updated;
    }
}
