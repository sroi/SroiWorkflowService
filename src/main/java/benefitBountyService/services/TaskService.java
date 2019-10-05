package benefitBountyService.services;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.exceptions.TaskNotFoundException;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import benefitBountyService.models.dtos.TaskTO;
import benefitBountyService.models.dtos.UserTO;
import benefitBountyService.utils.Constants;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import com.mongodb.client.model.Aggregates.*;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public boolean checkTaskById(String taskId){
        boolean found = false;
        Task task = taskRepository.findById(taskId);
        if (task != null){
            logger.info("Task found. Below are Task details: "+ task);
            found = true;
        }
        return found;
    }

    public Task getTaskDetailsById(String taskId) throws ResourceNotFoundException {
        if (ObjectId.isValid(taskId)) {
            TaskTO taskTO = null;
//        Task task = taskRepository.findById(taskId);
            Task task = taskRepository.fetchByTaskId(taskId);
            if (task != null) {
//            taskTO = getTaskToFromTask(task);
                logger.info("Task found. Below are Task details: " + task);
            } else {
                logger.warn("Task with id '" + taskId + "' is not present.");
                throw new TaskNotFoundException("Task not present...");
            }
            return task;
        } else {
            String errMsg = "Task ID '" + taskId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }

    public List<TaskTO> getTasksDetailsByName(String name) throws ResourceNotFoundException{
        List<Task> tasks =  null;
        List<TaskTO> taskTOList = null;
        tasks = taskRepository.findByName(name);
        if(tasks.isEmpty()) {
            logger.info("Task with name '" + name + "' doesn't exist.");
            throw new ResourceNotFoundException();
        } else {
            taskTOList = tasks.parallelStream().map(task -> getTaskToFromTask(task)).collect(Collectors.toList());
            logger.info("Below Tasks are found: \n" + taskTOList);
        }

        return taskTOList;
    }

    public List<Task> getTasksDetailsByProject(String projectId) throws ResourceNotFoundException{
        List<TaskTO> taskList = null;
        List<Task> tasks = null;
        if (ObjectId.isValid(projectId)) {
            tasks = getTasks(projectId);
//            taskList = tasks.stream().map(task -> getTaskToFromTask(task)).collect(Collectors.toList());
        } else {
            String errMsg = "Project ID '" + projectId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        return tasks;
    }

//    private TaskTO getTaskToFromTask(Task task) {
//
//    }

    private TaskTO getTaskToFromTask(Task task){
        PTUserTO aprTO = getApproverTOForTask(task);
//        List<PTUserTO> vols = getVolunteersTOForTask(task);

        TaskTO taskTO = new TaskTO(task.getTaskId().toString(),task.getName(), task.getDescription(), task.getProjectId(), task.getActivityLabel(), task.getStartDate(), task.getEndDate(), task.getLocation(),
                null, null, task.getStatus(), task.getCreated_by(), task.getCreated_on(), task.getUpdated_by(), task.getUpdated_on());
        return taskTO;
    }

    private List<PTUserTO> getVolunteersTOForTask(Task task) {
        List<PTUserTO> volunteers = null;
        if (task.getVolunteers() != null && task.getVolunteers().isEmpty()) {
            volunteers = task.getVolunteers().stream().map(volId -> getUserTOForTask(volId.toString())).collect(Collectors.toList());
//            PTUserTO vol = getUserTOForTask()
        } else {
            logger.info("This task "+ task.getTaskId() +" doesn't have volunteers");
        }
        return volunteers;
    }

    private PTUserTO getUserTOForTask(String userTOId){
        PTUserTO aprTO = null;
        User apr = userService.getUserById(userTOId);
        if (apr != null) {
            aprTO = new PTUserTO(apr.get_id(), apr.getName(), apr.getEmail(), apr.getPhoneNo());
        }
        return aprTO;
    }

    private PTUserTO getApproverTOForTask(Task task) {
        PTUserTO aprTO = null;
        if (task.getApprover() != null) {
            aprTO = getUserTOForTask(task.getApprover().toString());
//            aprTO = new PTUserTO(apr.get_id(), apr.getName(), apr.getEmailId(), apr.getPhoneNo());
        } else {
            logger.info("This task "+ task.getTaskId() +" doesn't have approver");
        }
        return aprTO;
    }

    private List<Task> getTasks(String projId) throws ResourceNotFoundException {
        List<Task> tasks = taskRepository.findByProjectId(projId);
        if (tasks != null && tasks.isEmpty()) {
            logger.info("Provided project doesn't have tasks created.");
            throw new TaskNotFoundException("Tasks Not found...");
        } else {
            logger.info("Provided project consists of following tasks:\n" + tasks);
        }
        return tasks;
    }

    public int deleteTask(String taskId) {
        int deleteStatus = -1;
        if (ObjectId.isValid(taskId)) {
            Task task = taskRepository.findById(taskId);
//        Todo: create TaskNotFoundException
//        Todo: logic to check if task is in valid state to delete and set foundTask flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);

            if(task != null){
                if (task.getStatus().equalsIgnoreCase(Constants.STATUS.CREATED.toString())) {
                    logger.info("Deleting task with id '" + taskId + "'.");
                    long deletedCount = taskRepository.deleteById(taskId);
                    if (deletedCount > 0) {
                        logger.info("successful deletion of task : " + taskId);
                        deleteStatus = 0;
                    } else {
                        logger.info("Task not deleted. Some unknown issue.Please check service logs");
                    }
                } else {
                    String errMsg = "Task with id '" + taskId + "' can not be deleted. It is in "+ task.getStatus() + " state";
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
                }
            } else {
                String errMsg = "Task with id '" + taskId + "' is not present.";
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg);
//                return 1; // failed- Task not found. Please refresh Task table.
            }
        } else {
            String errMsg = "Task ID '" + taskId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        //return 2; failed- Task can not be deleted. It is in <state> state.
        return deleteStatus;
    }

    public int saveOrUpdate(TaskTO taskTO) {
        int returnVal = 1;
        logger.info("Following task details have been received from User: "+taskTO);
        // Todo : To remove checkTaskById(project.getProjectId() from below
        // Todo: Logic for which fields to allow to be updated
        // Todo : No check for ProjectId.
//        TaskTO taskTO = null;
        Task task = null;
        List<User> users = userService.getUsers();
        Map<String, User> userMap = null;
        if (!users.isEmpty()) {
            userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
        }
        if (!StringUtils.isEmpty(taskTO.getTaskId())) {
            task = updateTaskFromTaskTO(taskTO, userMap);
            logger.info("Updating existing task.");
        } else {
            task = saveTaskFromTaskTO(taskTO, userMap);
            logger.info("New Task will be created.");
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Following task has been saved successfully: \n"+savedTask);
        //return project;
        if (savedTask != null)
            returnVal = 0;
        return returnVal;
    }

    @Transactional
    private Task saveTaskFromTaskTO(TaskTO taskTO, Map<String, User> userMap) {
        String loggedInUser = "admin";

        String apprId = checkAndSaveApprover(taskTO, userMap);

        List<String> volunteers = checkAndSaveVolunteers(taskTO, userMap);

        Task task = new Task(ObjectId.get(),taskTO.getName(), taskTO.getDescription(), taskTO.getProjectId(), taskTO.getActivityLabel(), taskTO.getStartDate(), taskTO.getEndDate(), taskTO.getLocation(),
                new ObjectId(apprId), volunteers, taskTO.getStatus(), loggedInUser, new Date(), loggedInUser, new Date());

        return task;
    }

    @Transactional
    private Task updateTaskFromTaskTO(TaskTO taskTO, Map<String, User> userMap) {
        String loggedInUser = "admin";
        Task task = null;
        Task existingTask = taskRepository.findById(taskTO.getTaskId());
        if (existingTask != null) {
            String apprId = checkAndSaveApprover(taskTO, userMap);

            List<String> volunteers = checkAndSaveVolunteers(taskTO, userMap);

            task = new Task(new ObjectId(taskTO.getTaskId()),taskTO.getName(), taskTO.getDescription(), taskTO.getProjectId(), taskTO.getActivityLabel(), taskTO.getStartDate(), taskTO.getEndDate(), taskTO.getLocation(),
                    new ObjectId(apprId), volunteers, taskTO.getStatus(), loggedInUser, new Date(), existingTask.getCreated_by(), existingTask.getCreated_on());
        } else {
            String errMsg = "This task is not present in Database for Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId();
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
        }
        return task;
    }

    private String checkAndSaveApprover(TaskTO taskTO, Map<String, User> userMap) {
        String apprId = null;
        User appr = null;
        if (taskTO.getApprover() != null) {
            if (!StringUtils.isEmpty(taskTO.getApprover().getId())) {
                appr = userMap.get(taskTO.getApprover().getId());
                apprId = appr.get_id();//Assuming approver is always present in UserMap
            } else {
                // This is new approver
                if (!StringUtils.isEmpty(taskTO.getApprover().getEmail())) {
                    UserTO user = userService.getUserByEmail(taskTO.getApprover().getEmail());
                    if(user != null) {
                        String errMsg = "User already exist with email Id: " + taskTO.getApprover().getEmail() + " for Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId();
                        logger.info(errMsg);
                        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
                    }
//                    appr = userService.saveApprover(taskTO.getApprover());
                    apprId = appr.get_id();
                } else {
                    logger.info("Email Id must be provided for Approver for Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId());
                }
            }
        } else {
            logger.info("Approver details are not provided while creating Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId());
        }
        return apprId;
    }

    private List<String> checkAndSaveVolunteers(TaskTO taskTO, Map<String, User> userMap) {
        List<PTUserTO> volunteersWithId = null, volunteersWOId = null;
        List<PTUserTO> validNewVols = null;
        List<String> existingVols = null, newVolsId = null;
        List<String> volunteers = new ArrayList<>();
        if(!taskTO.getVolunteers().isEmpty()) {
            volunteersWithId = taskTO.getVolunteers().parallelStream().filter(vol -> !StringUtils.isEmpty(vol.getId())).collect(Collectors.toList());
            existingVols = volunteersWithId.stream().filter(vol -> userMap.get(vol.getId()) != null).map(vol -> vol.getId()).collect(Collectors.toList());
            volunteers.addAll(existingVols);

            volunteersWOId = taskTO.getVolunteers().parallelStream().filter(vol -> StringUtils.isEmpty(vol.getId())).collect(Collectors.toList());
            validNewVols = volunteersWOId.stream().filter(vol -> !StringUtils.isEmpty(vol.getEmail())).collect(Collectors.toList());
            if (validNewVols.size() == volunteersWOId.size()) {
                newVolsId =  saveVolunteers(validNewVols);
                volunteers.addAll(newVolsId);
            } else {
                String errMsg = "Email IDs of some/all volunteers are not provided. Please provide for all.";
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
            }

        } else {
            volunteers = null;
            logger.info("Approver details are not provided while creating Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId());
        }
        return volunteers;
    }

    private List<String> saveVolunteers(List<PTUserTO> vols) {
        List<User> newVols = userService.saveVolunteers(vols);
        return newVols.parallelStream().map(User::get_id).collect(Collectors.toList());
    }

    public List<Task> getTasksDetailsByLogin(String userId, String role) {
        List<TaskTO> taskList = null;
        List<Task> tasks = null;
        if (ObjectId.isValid(userId)) {
            /*if (role.equalsIgnoreCase(Constants.ROLES.APPROVER.name())) {
                logger.info("Finding tasks for User '" + userId + "' against Approver role");
                tasks = getTasksForApprover(userId);
                logger.info("Total tasks found : " + tasks.size());
            } else if (role.equalsIgnoreCase(Constants.ROLES.VOLUNTEER.name())) {
                logger.info("Finding tasks for User '" + userId + "' against Volunteer role");
                tasks = getTasksForStakeholder(userId);
                logger.info("Total tasks found : " + tasks.size());
            } else {
                String errMsg = role + " is incorrect role to see tasks";
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
            }*/
            tasks = taskRepository.getTasksPerRoles(userId, role);
            logger.info("Total tasks found : " + tasks.size());
            //tasks = getTasks(userId);
        } else {
            String errMsg = "User ID '" + userId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        return tasks;
    }

    private List<Task> getTasksForStakeholder(String userId) {
        return taskRepository.getTasksForVolunteer(userId);
    }

    private List<Task> getTasksForApprover(String userId) {
        return taskRepository.getTasksForApprover(userId);
    }
}
