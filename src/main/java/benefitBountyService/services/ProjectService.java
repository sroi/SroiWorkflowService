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

import java.util.*;
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

                    ProjectTO projectTO = new ProjectTO(prj.getProjectId(), prj.getName(), prj.getAreaOfEngagement(), prj.getSummary(), prj.getStartDate(), prj.getEndDate(),
                            prj.getBudget(), prj.getCorporate(), prj.getLocation(), stHolder, pocTo, prj.getStatus(), prj.getRating(), prj.getUpdatedBy(), prj.getUpdatedOn(), prj.getCreatedBy(), prj.getCreatedOn());
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

    public List<ProjectTO> getProjectByUser() {
        String loggedInUser = null;
        List<ProjectTO> projectTOList = new ArrayList<>();
        List<Project> projects = projectRepository.findByCreatedBy(loggedInUser);
        return projectTOList;
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
        projectTO = new ProjectTO(prj.getProjectId(), prj.getName(), prj.getAreaOfEngagement(), prj.getSummary(), prj.getStartDate(), prj.getEndDate(), prj.getBudget(),
                prj.getCorporate(), prj.getLocation(), stHolder, poc, prj.getStatus(), prj.getRating(), prj.getUpdatedBy(), prj.getUpdatedOn(), prj.getCreatedBy(), prj.getCreatedOn());

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

        Project project = null;
        if (prjTO.getProjectId() != null) {
            Optional<Project> prjOpnl = projectRepository.findById(prjTO.getProjectId());
            project = prjOpnl.isPresent() ? prjOpnl.get() : null;
        }
        Project savedProj = saveOrUpdateProject(prjTO, project);
        logger.info("Following project has been saved successfully: \n"+savedProj);
        //return project;
        if (savedProj != null)
            returnVal = 0;
        return returnVal;
    }

    @Transactional
    private Project saveOrUpdateProject(ProjectTO prjTO, Project project) {
        //ToDo: To support server locale for storing Date for created_on and updated_on
        String loggedInUser = "admin";
        Project prj = null;
        if (prjTO.getProjectId() != null && project != null) {
            prjTO = saveOrUpdateProjectUsers(prjTO);
            String stHldId = (prjTO.getStakeholder() != null) ? prjTO.getStakeholder().getId() : Constants.EMPTY;
            String pocId = prjTO.getPointOfContact() != null ? prjTO.getPointOfContact().getId() : Constants.EMPTY;
            logger.info("Updating existing project...");
            prj = new Project(new ObjectId(prjTO.getProjectId()), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), stHldId, pocId, prjTO.getStatus(), prjTO.getRating(), loggedInUser, new Date(), project.getCreatedBy(), project.getCreatedOn());

        } else {
            prjTO = saveOrUpdateProjectUsers(prjTO);
            Double rating = 0.0d;//Double.MIN_VALUE;
            logger.info("Creating new Project..." + prjTO);
            String stHldId = (prjTO.getStakeholder() != null) ? prjTO.getStakeholder().getId() : Constants.EMPTY;
            String pocId = prjTO.getPointOfContact() != null ? prjTO.getPointOfContact().getId() : Constants.EMPTY;
            prj = new Project(ObjectId.get(), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), stHldId, pocId, Constants.CR_STATUS, rating, loggedInUser, new Date(), loggedInUser, new Date());
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
        List<User> users = userService.getUsers();
        Map<String, User> userMap = null;
        if (!users.isEmpty()) {
            userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
        }
        if (toStakeholder != null && toStakeholder.getEmail() != null) {//&& !toStakeholder.getUserId().equals(prj.getStakeholder())){
            if (toStakeholder.getId() != null) {
                //stkHldId = toStakeholder.getId();
                User sHolder = userMap.get(toStakeholder.getId());
                if (sHolder != null) {
                    if (!sHolder.getStakeholder().equals(Constants.YES))
                        userService.saveOrUpdateUser(sHolder);
                    stkHldId = sHolder.get_id();
                } else {
                    logger.info("Todo : Run away from this flow and tell user to provide details. The combination of name and id for below user doesn't exist=> " + toStakeholder.getId() + " and " + toStakeholder.getName());
                    // Todo : Run away from this flow and tell user to provide details
                }
            } else {
                logger.info("Saving stakeholder details for Project: "+prjTO.getName()+" <-> " + prjTO.getLocation());
                stakeHolder = userService.saveStakeHolder(toStakeholder);
                stkHldId = stakeHolder.get_id();
            }
            toStakeholder.setId(stkHldId);
        } else {
            logger.info("No stakeholder details are provided in Project. At least emailId should be provided.");
        }

        if (toPOC != null && toPOC.getEmail() != null) {//&& !toStakeholder.getUserId().equals(prj.getStakeholder())){
            if (toPOC.getId() != null) {
                pocId = toPOC.getId();
            } else {
                logger.info("Saving Point Of Contact details for Project: "+prjTO.getName()+" <-> " + prjTO.getLocation());
                poc = userService.savePOC(toPOC);
                pocId = poc.get_id();
            }
            toPOC.setId(pocId);
        } else {
            logger.info("No POC details are provided in Project. At least emailId should be provided.");
        }
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
