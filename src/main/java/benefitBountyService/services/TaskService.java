package benefitBountyService.services;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.exceptions.TaskNotFoundException;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import benefitBountyService.models.dtos.TaskTO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public boolean checkTaskById(String taskId){
        boolean found = false;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            logger.info("Task found. Below are Task details: "+ tskOpnl.get());
            found = true;
        }
        return found;
    }

    public TaskTO getTaskDetailsById(String taskId) throws ResourceNotFoundException {
        TaskTO taskTO = null;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            taskTO = getTaskToFromTask(tskOpnl.get());
            logger.info("Below are Task details: \n"+ taskTO);
        } else {
            logger.warn("Task with id '" + taskId + "' is not present.");
            throw new TaskNotFoundException("Task not present...");
        }
        return taskTO;
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

    public List<TaskTO> getTasksDetailsByProject(String projectId) throws ResourceNotFoundException{
        List<Task> tasks = getTasks(projectId);
        List<TaskTO> taskList = null;
        taskList = tasks.stream().map(task -> getTaskToFromTask(task)).collect(Collectors.toList());
        return taskList;
    }

//    private TaskTO getTaskToFromTask(Task task) {
//
//    }

    private TaskTO getTaskToFromTask(Task task){
        PTUserTO aprTO = getApproverTOForTask(task);
        List<PTUserTO> vols = getVolunteersTOForTask(task);

        TaskTO taskTO = new TaskTO(new ObjectId(task.getTaskId()),task.getName(), task.getDescription(), task.getProjectId(), task.getActivityLabel(), task.getStartDate(), task.getEndDate(), task.getLocation(),
                aprTO, vols, task.getCreated_by(), task.getCreated_on(), task.getUpdated_by(), task.getUpdated_on());
        return taskTO;
    }

    private List<PTUserTO> getVolunteersTOForTask(Task task) {
        List<PTUserTO> volunteers = null;
        if (!task.getVolunteers().isEmpty()) {
            volunteers = task.getVolunteers().stream().map(volId -> getUserTOForTask(volId)).collect(Collectors.toList());
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
            aprTO = new PTUserTO(apr.get_id(), apr.getName(), apr.getEmailId(), apr.getPhoneNo());
        }
        return aprTO;
    }

    private PTUserTO getApproverTOForTask(Task task) {
        PTUserTO aprTO = null;
        if (task.getApprover() != null) {
            aprTO = getUserTOForTask(task.getApprover());
//            aprTO = new PTUserTO(apr.get_id(), apr.getName(), apr.getEmailId(), apr.getPhoneNo());
        } else {
            logger.info("This task "+ task.getTaskId() +" doesn't have approver");
        }
        return aprTO;
    }

    private List<Task> getTasks(String projId) throws ResourceNotFoundException {
        List<Task> tasks = taskRepository.findByProjectId(projId);
        if (tasks.isEmpty()) {
            logger.info("Provided project doesn't have tasks created.");
            throw new TaskNotFoundException("Tasks Not found...");
        } else {
            logger.info("Provided project consists of following tasks:\n" + tasks);
        }
        return tasks;
    }

    public int deleteTask(String taskId) {
        boolean foundTask = checkTaskById(taskId);
//        Todo: create TaskNotFoundException
//        Todo: logic to check if task is in valid state to delete and set foundTask flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);
        if(foundTask){
            logger.info("Deleting task with id '" + taskId + "'.");
            taskRepository.deleteById(taskId);
            return 0; // successful deletion
        } else {
            logger.info("Task with id '" + taskId + "' is not present.");
            return 1; // failed- Task not found. Please refresh Task table.
        }
        //return 2; failed- Task can not be deleted. It is in <state> state.
    }

    public int saveOrUpdate(TaskTO taskTO) {
        int returnVal = 1;
        logger.info("Following task details have been received from User: "+taskTO);
        // Todo : To remove checkTaskById(project.getProjectId() from below
        // Todo: Logic for which fields to allow to be updated
//        TaskTO taskTO = null;
        Task task = null;
        if (task.getTaskId() != null && checkTaskById(task.getTaskId())) {
            task = createTaskFromTaskTO(taskTO);
            logger.info("Updating existing task.");
        } else {
            task = createTaskFromTaskTO(taskTO);
            logger.info("New Task will be created.");
            task.setTaskId(ObjectId.get());
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Following task has been saved successfully: \n"+savedTask);
        //return project;
        if (savedTask != null)
            returnVal = 0;
        return returnVal;
    }

    private Task createTaskFromTaskTO(TaskTO taskTO) {
        List<String> volunteers = taskTO.getVolunteers().stream().map(vol -> vol.getId()).collect(Collectors.toList());
        Task task = new Task(new ObjectId(taskTO.getTaskId()),taskTO.getName(), taskTO.getDescription(), taskTO.getProjectId(), taskTO.getActivityLabel(), taskTO.getStartDate(), taskTO.getEndDate(), taskTO.getLocation(),
                taskTO.getApprover().getId(), volunteers, taskTO.getCreated_by(), taskTO.getCreated_on(), taskTO.getUpdated_by(), taskTO.getCreated_on());

        return task;
    }
}
