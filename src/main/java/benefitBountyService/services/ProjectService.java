package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getProjects(String userId, String role){
        List<Project> projects = null;
        List<User> users = null;
        logger.info("Retrieving projects...");
        try {
            projects = projectRepository.findAll(userId, role);
        } catch (MongoException ex) {
            logger.error("Failure while loading projects in Mongo. {}");
        }
        logger.info("Below are projects details... \n" + projects);

        return projects;
    }

    public boolean checkProjectById(String projectId){
        boolean found = false;
        Project prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl != null){
            logger.info("Project found. Below are project details: "+prjOpnl);
            found = true;
        }
        return found;
    }

    public Project getProjectById(String projectId) {
        Project project = projectRepository.findById(projectId);
        if (project != null){
            logger.info("Below are Project details: "+ project);
        } else {
            logger.warn("Project with id '" + projectId + "' is not present.");
            //throw new Exception();
        }
        return project;
    }

    public ProjectTO getProjectDetailsById(String projectId) throws Exception  {
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
                ptUserTO = new PTUserTO(user.get_id(), user.getName(), user.getEmail(), user.getPhoneNo());
        }
        return ptUserTO;
    }

    public int deleteProject(String projectId) {
        int status = -1;
        if (!ObjectId.isValid(projectId)) {
            throwInvalidError(projectId);
        }
        Project project = getProjectById(projectId);
        if (projectId == null) {
            throwProjectNotFound(projectId);
        }
        if (!project.getStatus().equalsIgnoreCase(Constants.STATUS.CREATED.toString())) {
            String errMsg = "Project deletion failed as it is in '" + project.getStatus() + "' state.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
        }
        logger.info("Deleting project with id '" + projectId + "'.");
        long count = projectRepository.deleteById(projectId);
        if (count > 0) {
            status = 0;
        }
        return status;
    }

    private void throwInvalidError(String id) {
        String errMsg = "Project ID '" + id + "' is not valid input.";
        logger.info(errMsg);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
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
            project = projectRepository.findById(prjTO.getProjectId());
//            project = prjOpnl != null? prjOpnl : null;
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
                    prjTO.getLocation(), new ObjectId(stHldId), new ObjectId(pocId), prjTO.getStatus(), prjTO.getRating(), loggedInUser, new Date(), project.getCreatedBy(), project.getCreatedOn());

        } else {
            prjTO = saveOrUpdateProjectUsers(prjTO);
            Double rating = 0.0d;//Double.MIN_VALUE;
            logger.info("Creating new Project..." + prjTO);
            String stHldId = (prjTO.getStakeholder() != null) ? prjTO.getStakeholder().getId() : Constants.EMPTY;
            String pocId = prjTO.getPointOfContact() != null ? prjTO.getPointOfContact().getId() : Constants.EMPTY;
            prj = new Project(ObjectId.get(), prjTO.getName(), prjTO.getAreaOfEngagement(), prjTO.getSummary(), prjTO.getStartDate(), prjTO.getEndDate(), prjTO.getBudget(), prjTO.getCorporate(),
                    prjTO.getLocation(), new ObjectId(stHldId), new ObjectId(pocId), Constants.CR_STATUS, rating, loggedInUser, new Date(), loggedInUser, new Date());
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

    public int updateProjectStatus(String projectId, String status, String role, String comment, String rating) {
        int updated = -1;

        //Todo : Dummy user to be removed after session - management
        User loggedInUser = new User();// Todo : to be get from session.
        loggedInUser.setEmail("sawannai@gmail.com");
        loggedInUser.setUserId("sawannai@gmail.com");
        loggedInUser.setName("sawan nai");
        loggedInUser.set_id(new ObjectId("5d9984b61c9d440000d024be"));

        try {
            if (ObjectId.isValid(projectId)) {
                if (role.equalsIgnoreCase(Constants.ROLES.STAKEHOLDER.toString())) {
                    updateStatusForStakeholder(loggedInUser, projectId, status, role, comment, rating);
                } else if (role.equalsIgnoreCase(Constants.ROLES.ADMIN.toString())) {
                    updateStatusForAdmin(loggedInUser, projectId, status, role, comment, rating);
                } else {
                    String errMsg = role + " is not allowed to change status at Project level.";
                    logger.warn(errMsg);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errMsg);
                }
            } else {
                throwInvalidError(projectId);
            }
        } catch (Exception e) {
            throwProjectNotFound(projectId);
        }
        return updated;
    }

    private int updateStatusForAdmin(User loggedInUser, String projectId, String status, String role, String comment, String rating) {
        int updated = -1;
        if (status.equalsIgnoreCase(Constants.STATUS.CLOSED.toString())) {
            Project project = getProjectById(projectId);
            if (project == null) {
                throwProjectNotFound(projectId);
            }
            if (status.equalsIgnoreCase(project.getStatus())) {
                throwProjectSameStatusError(projectId, status);
            }
            if (project.getStatus().equalsIgnoreCase(Constants.STATUS.APPROVED.toString())
                    || project.getStatus().equalsIgnoreCase(Constants.STATUS.ON_HOLD.toString())) {
                logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
                project.setStatus(status);
                updated = projectRepository.changeTaskStatus(loggedInUser, project, role, comment);
            } else {
                String errMsg = "Project with id '" + projectId + "' is in invalid state to close.";
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
            }
        } else if (status.equalsIgnoreCase(Constants.STATUS.ON_HOLD.toString())) {
            Project project = getProjectById(projectId);
            if (project == null) {
                throwProjectNotFound(projectId);
            }
            if (status.equalsIgnoreCase(project.getStatus())) {
                throwProjectSameStatusError(projectId, status);
            }
            if (!project.getStatus().equalsIgnoreCase(Constants.STATUS.CLOSED.toString())) {
                logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
                project.setStatus(status);
                updated = projectRepository.changeTaskStatus(loggedInUser, project, comment, rating);
            } else {
                String errMsg = "Project with id '" + projectId + "' is in invalid state to put on hold. Current status: "+ project.getStatus();
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
            }
        } else {
            String errMsg = "Admin can't change status to " + status;
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errMsg);
        }
        return updated;
    }

    private int updateStatusForStakeholder(User loggedInUser, String projectId, String status, String role, String comment, String rating) {
        int updated = -1;
        if (status.equalsIgnoreCase(Constants.STATUS.APPROVED.toString())) {
            Project project = getProjectById(projectId);
            if (project != null) {
                if (project.getStatus().equalsIgnoreCase(status)) {
                    String errMsg = "Project Id '" + projectId + "' is already in '"+ status + "' status.";
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
                }
                logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
                List<Task> tasks = taskRepository.findByProjectId(projectId);
                if (tasks!= null && !tasks.isEmpty()) {
                    long unapprovedTaskCount = tasks.stream().filter(t -> !t.getStatus().equalsIgnoreCase(Constants.STATUS.APPROVED.toString())).count();
                    if (unapprovedTaskCount == 0) {
                        project.setStatus(status);
                        project.setRating(Double.parseDouble(rating));
                        updated = projectRepository.changeTaskStatus(loggedInUser, project, role, comment);
                    } else {
                        String errMsg = "Changing task status from '"+ project.getStatus() + "' to status '" + status+ "' is not allowed. Unapproved no. of Tasks: "+unapprovedTaskCount;
                        logger.info(errMsg);
                        throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
                    }
                } else {
                    String errMsg = "Project Id '" + projectId + "' doesn't have tasks created in it. So can't change status.";
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
                }

            } else {
                throwProjectNotFound(projectId);
            }
        } else if (status.equalsIgnoreCase(Constants.STATUS.REJECTED.toString())) {
            Project project = getProjectById(projectId);
            if (project != null) {
                if (project.getStatus().equalsIgnoreCase(status)) {
                    String errMsg = "Project Id '" + projectId + "' is already in '" + status + "' status.";
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
                }
                logger.info("Updating status of project with id '"+ projectId + "' from " + project.getStatus() + " to " + status);
                project.setStatus(status);
                project.setRating(0.0); // Resetting rating to 0.0 as it is being rejected.
                updated = projectRepository.changeTaskStatus(loggedInUser, project, role, comment);
//                projectRepository.save(project);
                updated = 0;
            } else {
                throwProjectNotFound(projectId);
            }

        } else {
            String errMsg = "Stakeholder is not allowed to change status to " + status;
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errMsg);
        }
        return updated;
    }

    private void saveProjectActivity(Project savedPrj) {

    }

    private void throwProjectSameStatusError(String projectId, String status) {
        String errMsg = "Project Id '" + projectId + "' is already in '"+ status + "' status.";
        logger.warn(errMsg);
        throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, errMsg);
    }

    private void throwProjectNotFound(String id) {
        String errMsg = "Project ID '" + id + "' doesn't exist.";
        logger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg);
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }
}
