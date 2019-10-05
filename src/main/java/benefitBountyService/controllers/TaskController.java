package benefitBountyService.controllers;

import benefitBountyService.exceptions.BadInputException;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.TaskTO;
import benefitBountyService.services.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Description - To find list of tasks satisfying given projectId condition.
     * Param - Project_id (in String format)
     * Return Value -  Return list of tasks
     */
    @GetMapping(value = "/tasks")
    public List<Task> getTasksDetailsByProject(@RequestParam("pid") String projectId) throws BadInputException {
//        if (projectId == null)
//            throw new BadInputException("Project Id");
        List<Task> tasks = null;
        try {
            tasks = taskService.getTasksDetailsByProject(projectId);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This project Id doesn't have tasks.",e);
        }
        return tasks;
    }

    /**
     * Description - To find list of tasks satisfying given projectId condition.
     * Param - Project_id (in String format)
     * Return Value -  Return list of tasks
     */
    @GetMapping(value = "/fetch")
    public List<Task> fetchTasksByLogin(@RequestParam("uid") String userId, @RequestParam("role") String role) {
        List<Task> tasks = taskService.getTasksDetailsByLogin(userId, role);
        return tasks;
    }

    /**
     * Description - To find task details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @GetMapping(value = "/task")
    public Task getTasksDetailsByTaskId(@RequestParam("tid") String taskId) {
        TaskTO taskTO = null;
        Task task = null;
        try {
            task = taskService.getTaskDetailsById(taskId);
            return task;
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This task Id doesn't exist.", e);
        }
    }

    /**
     * Description - To find list of tasks satisfying given task name.
     * Param - task_name (in String format)
     * Return Value -  Return list of tasks
     */
    /*@GetMapping(value = "/tasks/name" , method=RequestMethod.GET)
    public List<TaskTO> getTasksDetailsByName(@RequestParam("tname") String taskName){
        List<TaskTO> tasks = null;
        try{
            tasks = taskService.getTasksDetailsByName(taskName);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide proper task name.", e);
        }
        return tasks;
    }*/

    /**
     * Description - To create new task.
     * Param - task object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @PostMapping(value = "/create")
    public int addTask(@RequestBody TaskTO task){
        return taskService.saveOrUpdate(task);
    }

    /**
     * Description - To delete existing task.
     * Param - Task (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Task table.
     *                     -> 2 - failed- Task can not be deleted. It is in <state> state.
     */
    @DeleteMapping(value = "/delete")
    public int deleteTask(@RequestParam("tid") String taskId){
        return taskService.deleteTask(taskId);
    }
}
