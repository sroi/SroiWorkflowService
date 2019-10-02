package benefitBountyService.controllers;

import benefitBountyService.exceptions.BadInputException;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Activity;
import benefitBountyService.models.Task;
import benefitBountyService.models.dtos.TaskTO;
import benefitBountyService.services.ActivityService;
import benefitBountyService.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivityService activityService;
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

    @GetMapping("/fetch")
    public void getTaskData(@RequestParam("tid") String taskId, @RequestParam("pid") String uid) {
        taskService.getTaskDetailsByTaskAndProject(taskId, uid);
    }


    @RequestMapping(value = "/approver" , method=RequestMethod.GET)
    public List<Task> getTasksDetailsByApprover(@RequestParam("approver") String approver) {
        /*if (taskId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Approver can not be empty");*/
        List<Task> task = null;
        try {
            task = taskService.getTaskDetailsByApprover(approver);
            return task;
        } catch (ResourceNotFoundException e) {
//            System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide correct approver Id.", e);
        }
    }
    @RequestMapping(value = "/activity" , method=RequestMethod.GET)
    public List<Activity> getActivityDetailsBytask(@RequestParam("taskId") String taskId) {

        if (taskId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "task can not be empty");
        List<Activity> activityList = null;
        try {
            ///  activityList=new ArrayList<>();
            System.out.println("Below are Task details:getActivityDetailsBytask \n"+ taskId );
            activityList = activityService.getActivityByTask(taskId);

            return activityList;
        }  catch (Exception e) {
//         System.out.println("catch (TaskNotFoundException e)");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide correct task Id.", e);
        }
    }

}
