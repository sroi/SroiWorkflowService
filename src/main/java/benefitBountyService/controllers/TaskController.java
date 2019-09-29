package benefitBountyService.controllers;

import benefitBountyService.exceptions.BadInputException;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.dtos.TaskTO;
import benefitBountyService.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @RequestMapping(value = "/tasks" , method=RequestMethod.GET)
    public List<TaskTO> getTasksDetailsByProject(@RequestParam("pid") String projectId) throws BadInputException {
//        if (projectId == null)
//            throw new BadInputException("Project Id");
        List<TaskTO> tasks = null;
        try {
            tasks = taskService.getTasksDetailsByProject(projectId);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This project Id doesn't have tasks.",e);
        }
        return tasks;
    }

    /**
     * Description - To find task details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @RequestMapping(value = "/task" , method=RequestMethod.GET)
    public TaskTO getTasksDetailsByTaskId(@RequestParam("tid") String taskId) {
        /*if (taskId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task Id can not be empty");*/
        TaskTO task = null;
        try {
            task = taskService.getTaskDetailsById(taskId);
            return task;
        } catch (ResourceNotFoundException e) {
//            System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This task Id doesn't exist.", e);
        }
    }

    /**
     * Description - To find list of tasks satisfying given task name.
     * Param - task_name (in String format)
     * Return Value -  Return list of tasks
     */
    @RequestMapping(value = "/tasks/name" , method=RequestMethod.GET)
    public List<TaskTO> getTasksDetailsByName(@RequestParam("tname") String taskName){
        List<TaskTO> tasks = null;
        try{
            tasks = taskService.getTasksDetailsByName(taskName);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide proper task name.", e);
        }
        return tasks;
    }

    /**
     * Description - To create new task.
     * Param - task object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
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
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public int deleteTask(@RequestParam("tid") String taskId){
        return taskService.deleteTask(taskId);
    }
}
