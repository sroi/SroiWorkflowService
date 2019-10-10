package benefitBountyService.services;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import benefitBountyService.utils.Constants;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> getTasksDetailsByProject(String projectId) {
        List<Task> tasks;
        if (ObjectId.isValid(projectId)) {
            tasks = taskRepository.findByProjectId(projectId);;
        } else {
            String errMsg = "Project ID '" + projectId + "' is not valid input.";
            logger.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        return tasks;
    }

    public List<Task> getTasksDetailsByLogin(String userId, String role) {
        List<Task> tasks = null;
        if (ObjectId.isValid(userId)) {
            tasks = taskRepository.getTasksAfterLogin(userId, role);
            logger.info("Total tasks found : " + tasks.size());
        } else {
            String errMsg = "User ID '" + userId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        return tasks;
    }

    public Task getTaskDetailsById(String taskId) throws Exception {
        if (ObjectId.isValid(taskId)) {
            Task task = taskRepository.fetchByTaskId(taskId);
            if (task != null) {
                logger.info("Task found. Below are Task details: " + task);
            } else {
                logger.warn("Task with id '" + taskId + "' is not present.");
                throw new Exception("Task not present...");
            }
            return task;
        } else {
            String errMsg = "Task ID '" + taskId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }

    public int saveOrUpdate(Task taskTO) {
        int returnVal = 1;
        logger.info("Following task details have been received from User: "+ taskTO);
        // Todo: Logic for which fields to allow to be updated
        // Todo : No check for ProjectId.
        Task task = null;
        if (!StringUtils.isEmpty(taskTO.getTaskId())) {
            task = updateTaskFromTaskTO(taskTO);
            logger.info("Updating existing task.");
        } else {
            task = saveTaskFromTaskTO(taskTO);
            logger.info("New Task will be created.");
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Following task has been saved successfully: \n"+savedTask);
        if (savedTask != null)
            returnVal = 0;
        return returnVal;
    }

    public int deleteTask(String taskId) {
        int deleteStatus = -1;
        if (ObjectId.isValid(taskId)) {
            Task task = taskRepository.findById(taskId);
//        Todo: logic to check if task is in valid state to delete and set foundTask flag as per that

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



    @Transactional
    private Task saveTaskFromTaskTO(Task taskTO) {
        String loggedInUser = "admin";
        Task task = null;
        String apprId = null;
        ObjectId approver = null;
        List<String> volunteers = null;

        Map<String, User> userMap = null;
        if (taskTO.getApprover_info() != null || (taskTO.getVols_info().size() > 0)) {
            List<User> users = userService.getUsers();
            if (!users.isEmpty()) {
                userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
            }
            apprId = checkAndSaveApprover(taskTO, userMap);
            if (ObjectId.isValid(apprId)) {
                approver = new ObjectId(apprId);
            } else {
                approver = null;
                logger.info("Approver ID obtained is not valid : " + apprId);
            }

            volunteers = checkAndSaveVolunteers(taskTO, userMap);

        } else {
            logger.info("Approver and Stakeholder details are not provided while creating Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId());
        }
        task = new Task(ObjectId.get(),taskTO.getName(), taskTO.getDescription(), new ObjectId(taskTO.getProjectId()), taskTO.getActivityLabel(), taskTO.getStartDate(), taskTO.getEndDate(), taskTO.getLocation(),
                approver, volunteers, Constants.STATUS.CREATED.toString(), loggedInUser, new Date(), loggedInUser, new Date());
        return task;
    }

    @Transactional
    private Task updateTaskFromTaskTO(Task taskTO) {
        String loggedInUser = "admin";
        Task task = null;
        String apprId = null;
        ObjectId approver = null;
        List<String> volunteers = null;

        Task existingTask = taskRepository.findById(taskTO.getTaskId());
        if (existingTask != null) {
            Map<String, User> userMap = null;
            if (taskTO.getApprover() != null || (taskTO.getVols_info().size() > 0 && taskTO.getVols_info().get(0) != null)) {
                List<User> users = userService.getUsers();
                if (!users.isEmpty()) {
                    userMap = users.stream().collect(Collectors.toMap(User::get_id, user -> user));
                }

                apprId = checkAndSaveApprover(taskTO, userMap);
                if (apprId != null) {
                    approver = new ObjectId(apprId);
                } else {
                    approver = null;
                    logger.info("Approver ID obtained is not valid : " + apprId);
                }

                volunteers = checkAndSaveVolunteers(taskTO, userMap);

            } else {
                logger.info("Approver and Stakeholder details are not provided while creating Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId());
            }
            task = new Task(new ObjectId(taskTO.getTaskId()),taskTO.getName(), taskTO.getDescription(), new ObjectId(taskTO.getProjectId()), taskTO.getActivityLabel(), taskTO.getStartDate(),
                    taskTO.getEndDate(), taskTO.getLocation(), approver, volunteers, taskTO.getStatus(), existingTask.getCreated_by(), existingTask.getCreated_on(), loggedInUser, new Date());

        } else {
            String errMsg = "This task is not present in Database for Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId();
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
        }
        return task;
    }

    private String checkAndSaveApprover(Task taskTO, Map<String, User> userMap) {
        String apprId = null;
        if (taskTO.getApprover() != null) {
            User appr = null;
            if (!StringUtils.isEmpty(taskTO.getApprover_info().get_id())) {
                appr = userMap.get(taskTO.getApprover_info().get_id());
                apprId = appr.get_id();//Assuming approver is always present in UserMap
            } else {
                System.out.println("This is new approver");
                if (!StringUtils.isEmpty(taskTO.getApprover_info().getEmail())) {
                    User user = userService.getUserByEmail(taskTO.getApprover_info().getEmail());
                    if(user != null) {
                        String errMsg = "User already exist with email Id: " + taskTO.getApprover_info().getEmail() + " for Task: " + taskTO.getName() + " for Project: " + taskTO.getProjectId();
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

    private List<String> checkAndSaveVolunteers(Task taskTO, Map<String, User> userMap) {
        List<String> volunteers = new ArrayList<>();

        if(taskTO.getVolunteers().size() > 0) {
            List<User> volunteersWithId = null, volunteersWOId = null;
            List<User> validNewVols = null;
            List<String> existingVols = null, newVolsId = null;
            volunteersWithId = taskTO.getVols_info().parallelStream().filter(vol -> !StringUtils.isEmpty(vol.get_id())).collect(Collectors.toList());
            existingVols = volunteersWithId.stream().filter(vol -> userMap.get(vol.get_id()) != null).map(vol -> vol.get_id()).collect(Collectors.toList());
            volunteers.addAll(existingVols);

            volunteersWOId = taskTO.getVols_info().parallelStream().filter(vol -> StringUtils.isEmpty(vol.get_id())).collect(Collectors.toList());
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

    private List<String> saveVolunteers(List<User> vols) {
        List<User> newVols = userService.saveVolunteers(vols);
        return newVols.parallelStream().map(User::get_id).collect(Collectors.toList());
    }

    public int changeTaskStatus(String taskId, String role, String status, String comments, String timeSpent) {
        User loggedInUser = new User();// Todo : to be get from session.
        loggedInUser.setEmail("sawannai@gmail.com");
        loggedInUser.setUserId("sawannai@gmail.com");
        loggedInUser.setName("sawan nai");
        loggedInUser.set_id(new ObjectId("5d9984b61c9d440000d024be"));

        int statusChange = -1;
        if (ObjectId.isValid(taskId)) {
            Task taskFound = taskRepository.fetchByTaskId(taskId);
            if (role.equalsIgnoreCase(Constants.ROLES.APPROVER.toString())) {
                if (status.equalsIgnoreCase(Constants.STATUS.APPROVED.toString()) || status.equalsIgnoreCase(Constants.STATUS.REJECTED.toString())) {
                    if (!status.equalsIgnoreCase(taskFound.getStatus())) {
                        statusChange = taskRepository.changeTaskStatus(loggedInUser, taskFound, role, status, comments, timeSpent);
                    } else {
                        String errMsg = "Changing task status from '"+ taskFound.getStatus() + "' to status '" + status+ "' is not allowed.";
                        logger.info(errMsg);
                        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
                    }
                } else {
                    String errMsg = "Approver can't change status to " + status;
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errMsg);
                }

            } else if (role.equalsIgnoreCase(Constants.ROLES.VOLUNTEER.toString())) {
                String taskStatus = taskFound.getStatus();
                if (status.equalsIgnoreCase(Constants.STATUS.SUBMITTED.toString())) {
                    if (taskStatus.equalsIgnoreCase(Constants.STATUS.CREATED.toString()) || taskStatus.equalsIgnoreCase(Constants.STATUS.SUBMITTED.toString())
                        || taskStatus.equalsIgnoreCase(Constants.STATUS.REJECTED.toString())) {
                        statusChange = taskRepository.changeTaskStatus(loggedInUser, taskFound, role, status, comments, timeSpent);
                    } else {
                        String errMsg = "Changing task status from '"+ taskFound.getStatus() + "' to status '" + status+ "' is not allowed.";
                        logger.info(errMsg);
                        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
                    }
                } else {
                    String errMsg = "Volunteer can't change status to " + status;
                    logger.info(errMsg);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errMsg);
                }
            } else {
                String errMsg = "Invalid role is provided for Status change : " + role;
                logger.info(errMsg);
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, errMsg);
            }

        } else {
            String errMsg = "Task ID '" + taskId + "' is not valid input.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        return statusChange;
    }
}
