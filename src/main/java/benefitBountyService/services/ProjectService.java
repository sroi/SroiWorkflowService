package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.exceptions.SroiResourceNotFoundException;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import benefitBountyService.models.dtos.ProjectTO;
import benefitBountyService.utils.Constants;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            Map<String, User> userMap = null;
            if (!projects.isEmpty()){
                users = userService.getUsers();
                if (!users.isEmpty()) {
                    userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
                }

                for (Project prj : projects) {

                    logger.info("Retrieving data for Stakeholder of project");
                    PTUserTO stHolder = getPTUserTOFromMap(prj.getStakeholder(),userMap);

                    logger.info("Retrieving data for POC of project");
                    PTUserTO pocTo = getPTUserTOFromMap(prj.getPointOfContacts(),userMap);

                    ProjectTO projectTO = new ProjectTO(prj.getProjectId(), prj.getName(), prj.getAreaOfEngagement(), prj.getSummary(), prj.getStartDate(), prj
                            .getEndDate(), prj.getBudget(), prj.getCorporate(), prj.getLocation(), stHolder, pocTo, prj.getStatus());
                    projectTOs.add(projectTO);
                }
            } else {
                logger.info("collection 'projects' is empty. Please load some data through 'Create Project' page");
            }

        } catch (MongoException ex) {
            logger.error("Failure while loading projects in Mongo. {}");
        }
        logger.info("Below are projects details... \n" + projectTOs);
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

    public Project getProjectById(String projectId) throws ResourceNotFoundException {
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

    public ProjectTO getProjectDetailsById(String projectId) throws ResourceNotFoundException {
        ProjectTO projectTO = null;
        PTUserTO stHolder = null;
        PTUserTO poc = null;
        Project prj = getProjectById(projectId);

        List<User> users = userService.getUsers();
        if (!users.isEmpty()) {
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));

            logger.info("Retrieving data for Stakeholder of project");
            stHolder = getPTUserTOFromMap(prj.getStakeholder(),userMap);

            logger.info("Retrieving data for POC of project");
            poc = getPTUserTOFromMap(prj.getPointOfContacts(),userMap);

        }
        projectTO = new ProjectTO(prj.getProjectId(), prj.getName(), prj.getAreaOfEngagement(), prj.getSummary(), prj.getStartDate(), prj
                .getEndDate(), prj.getBudget(), prj.getCorporate(), prj.getLocation(), stHolder, poc, prj.getStatus());

        return projectTO;
    }

    private PTUserTO getPTUserTOFromMap(String userId, Map<String, User> userMap){
        PTUserTO ptUserTO = null;
        if (userId != null && !userMap.isEmpty()) {
            User user = userMap.get(userId);
            if (user != null)
                ptUserTO = new PTUserTO(user.get_id(), user.getName(), user.getEmailId(), user.getPhoneNo());
        }
        return ptUserTO;
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
        // Todo: Activity_capture need to be updated.

        boolean isProjectPresent = false;
        if (prjTO.getProjectId() != null) {
            Optional<Project> prjOpnl = projectRepository.findById(prjTO.getProjectId());
            isProjectPresent = prjOpnl.isPresent();
        }
        Project savedProj = saveOrUpdateProject(prjTO, isProjectPresent);
        logger.info("Following project has been saved successfully: \n"+savedProj);
        //return project;
        if (savedProj != null)
            returnVal = 0;
        return returnVal;
    }

    @Transactional
    private Project saveOrUpdateProject(ProjectTO prjTO, boolean isProjPresent) {
        Project prj = null;
        if (prjTO.getProjectId() != null && isProjPresent) {
            prjTO = saveOrUpdateProjectUsers(prjTO);
            logger.info("Updating existing project...");
            prj = new Project(new ObjectId(prjTO.getProjectId()), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), prjTO.getStakeholder().getId(), prjTO.getPointOfContact().getId(), prjTO.getStatus());

        } else {
            prjTO = saveOrUpdateProjectUsers(prjTO);
            logger.info("Creating new Project..." + prjTO);
            prj = new Project(ObjectId.get(), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(),
                    prjTO.getBudget(), prjTO.getCorporate(), prjTO.getLocation(), prjTO.getStakeholder().getId(), prjTO.getPointOfContact().getId(), Constants.CR_STATUS);
            //prj.setProjectId(ObjectId.get());
        }
        Project savedProj = projectRepository.save(prj);
        return savedProj;
    }

    private ProjectTO saveOrUpdateProjectUsers(ProjectTO prjTO) {
        String stkHldId = "", pocId = "";
        User stakeHolder = null;
        User poc = null;
        PTUserTO toStakeholder = prjTO.getStakeholder();
        PTUserTO toPOC = prjTO.getPointOfContact();
        if (toStakeholder != null && toStakeholder.getEmail() != null) {//&& !toStakeholder.getUserId().equals(prj.getStakeholder())){
            if (toStakeholder.getId() != null) {
                stkHldId = toStakeholder.getId();
            } else {
                logger.info("Saving stakeholder details for Project: "+prjTO.getName()+" <-> " + prjTO.getLocation());
                stakeHolder = userService.saveStakeHolder(toStakeholder);
                stkHldId = stakeHolder.get_id();
            }
        } else {
            logger.info("No stakeholder details are provided in Project.");
        }
        toStakeholder.setId(stkHldId);

        if (toPOC != null && toPOC.getEmail() != null) {//&& !toStakeholder.getUserId().equals(prj.getStakeholder())){
            if (toPOC.getId() != null) {
                pocId = toPOC.getId();
            } else {
                logger.info("Saving Point Of Contact details for Project: "+prjTO.getName()+" <-> " + prjTO.getLocation());
                poc = userService.savePOC(toPOC);
                pocId = poc.get_id();
            }
        } else {
            logger.info("No POC details are provided in Project.");
        }
        toPOC.setId(pocId);
        return prjTO;
    }

    public int updateProjectStatus(String projectId, String status) {
        int updated = 1;
        try {
            Project project = getProjectById(projectId);
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
