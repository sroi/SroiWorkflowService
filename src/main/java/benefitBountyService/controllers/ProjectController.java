package benefitBountyService.controllers;

import benefitBountyService.models.Project;
import benefitBountyService.models.dtos.ProjectTO;
import benefitBountyService.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
	
	@RequestMapping(value = "/all" , method=RequestMethod.GET)
	public List<Project> getProjects(@RequestParam("user_id") String userId, @RequestParam("Role") String role) {
        List<Project> projects = projectService.getProjects(userId, role);
        return projects;
    }

    /**
     * Description - To find Project details for given taskId.
     * Param - tid (in String format)
     * Return Value -  Return task details
     */
    @RequestMapping(value = "/get" , method=RequestMethod.GET)
    public ProjectTO getProjectDetailsByProjectId(@RequestParam("pid") String projectId) {
        ProjectTO project = null;
        try {
            project = projectService.getProjectDetailsById(projectId);
            return project;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please provide correct project Id", e);
        }
    }

    /**
     * Description - To create new projects.
     * Param - project object
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public int addProject(@RequestBody ProjectTO project){
        return projectService.saveOrUpdate(project);
    }

    /**
     * Description - To delete existing project.
     * Param - Project_id (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Project table.
     *                     -> 2 - failed- Project can not be deleted. It is in <state> state.
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public int deleteProject(@RequestParam("pid") String projectId){
        return projectService.deleteProject(projectId);
    }

    /**
     * Description - To update project status.
     * Param - Project Id (in String format)
     * Return Value -  int -> 0 - success
     *                     -> 1 - failed- Task not found. Please refresh Task table.
     *                     -> 2 - failed- Task can not be updated. It is in <state> state.
     */
    @PostMapping(value = "/status")
    public int updateProjectStatus(@RequestParam("pid") String projectId,
                                   @RequestParam("status") String status,
                                   @RequestParam(value = "role", required = false) String role){
        return projectService.updateProjectStatus(projectId, status, role);
    }

}
