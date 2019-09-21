package benefitBountyService.services;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Task;
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
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    public boolean checkTaskById(String taskId){
        boolean found = false;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            logger.info("Task found. Below are Task details: "+ tskOpnl.get());
            found = true;
        }
        return found;
    }

    public Task getTaskDetailsById(String taskId) throws ResourceNotFoundException {
        Task task = null;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            task = tskOpnl.get();
            logger.info("Below are Task details: \n"+ task);
        } else {
            logger.warn("Task with id '" + taskId + "' is not present.");
            throw new ResourceNotFoundException();
        }
        return task;
    }

    public List<Task> getTasksDetailsByName(String name) throws ResourceNotFoundException{
        List<Task> tasks =  null;
        tasks = taskRepository.findByName(name);
        if(tasks.isEmpty()) {
            logger.info("Task with name '" + name + "' doesn't exist.");
            throw new ResourceNotFoundException();
        } else
            logger.info("Below Tasks are found: \n" + tasks);
        return tasks;
    }

    public List<Task> getTasksDetailsByProject(String projectId) throws ResourceNotFoundException{
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            logger.info("Provided project doesn't have tasks created.");
            throw new ResourceNotFoundException();
        } else
            logger.info("Provided project consists of following tasks:\n" + tasks);
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

    public int createTask(Task task) {
        int returnVal = 1;
        logger.info("Following task details have been received from User: "+task);
        // Todo : To remove checkTaskById(project.getProjectId() from below
        // Todo: Logic for which fields to allow to be updated
        if (task.getTaskId() != null && checkTaskById(task.getTaskId()))
            logger.info("Updating existing task.");
        else {
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
}
