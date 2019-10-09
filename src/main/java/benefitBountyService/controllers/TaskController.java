package benefitBountyService.controllers;

import benefitBountyService.models.Task;
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

    /**
     * Description - To find list of tasks satisfying given projectId condition.
     * Param - Project_id (in String format)
     * Return Value -  Return list of tasks
     */
    @GetMapping(value = "/tasks")
    public List<Task> getTasksDetailsByProject(@RequestParam("pid") String projectId) {
        List<Task> tasks;
        try {
            tasks = taskService.getTasksDetailsByProject(projectId);
        } catch (ResponseStatusException ex ) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This project Id " + projectId + " doesn't have tasks.",e);
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
        List<Task> tasks;
        try {
            tasks = taskService.getTasksDetailsByLogin(userId, role);
        } catch (ResponseStatusException ex ) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user login " + userId + " doesn't have tasks.",e);
        }
        return tasks;
    }

    /**
     * Description - To find task details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @GetMapping(value = "/task")
    public Task getTasksDetailsByTaskId(@RequestParam("tid") String taskId) {
        Task task = null;
        try {
            task = taskService.getTaskDetailsById(taskId);
            return task;
        } catch (ResponseStatusException ex ) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This task Id " + taskId + " doesn't exist.", e);
        }
    }

    /**
     * Description - To create new task.
     * Param - task object
     * Return Value -  int -> 0 - succss
     *                     -> 1 - failed
     */
    @PostMapping(value = "/create")
    public int addTask(@RequestBody Task task) {
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
    public int deleteTask(@RequestParam("tid") String taskId) {
        return taskService.deleteTask(taskId);
    }

    /**
     * Description - To save status of task.
     * Param - Task (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @PutMapping(value = "/status")
    public int changeTaskStatus(@RequestParam("tid") String taskId,
                                @RequestParam(value = "role", required = false) String role,
                                @RequestParam("status") String status,
                                @RequestParam(value="comments", required = false) String comments,
                                @RequestParam(value = "time", required = false) String timeSpent) {
        int statusChange = -1;
        statusChange = taskService.changeTaskStatus(taskId, role, status, comments, timeSpent);

        return statusChange;
    }

}
