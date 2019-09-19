package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.dao.TaskRepository;
import benefitBountyService.exceptions.ResourceNotFoundException;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getProjects(){
//        Todo : get projects depending upon roles
        List<Project> projects = null;
        try {
            projects = projectRepository.findAll();
            if (projects.isEmpty())
                logger.info("collection 'projects' is empty. Please load some data through 'Create Project' page");
        } catch (MongoException ex) {
            logger.error("Failure while loading projects in Mongo. {}");
        }
        return projects;
    }

    public boolean getProjectById(String projectId){
        boolean found = false;
        Optional<Project> prjOpnl = projectRepository.findById(projectId);
        if (prjOpnl.isPresent()){
            logger.info("Below are project details: "+prjOpnl.get());
            found = true;
        }
        /*prjOpnl.ifPresent( prj -> {
            System.out.println("Value found: "+ prj);
            found = true;
        });*/
        return found;
    }

    public boolean getTaskById(String taskId){
        boolean found = false;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            logger.info("Below are Task details: "+ tskOpnl.get());
            found = true;
        }
        return found;
    }

    public Task getTaskDetailsById(String taskId) throws ResourceNotFoundException {
        Task task = null;
        Optional<Task> tskOpnl = taskRepository.findById(taskId);
        if (tskOpnl.isPresent()){
            task = tskOpnl.get();
            logger.info("Below are Task details: "+ task);
        } else {
            logger.warn("Task with id '" + taskId + "' is not present.");
            throw new ResourceNotFoundException();
        }
        return task;
    }

    public List<Task> getTasksDetailsByName(String name) {
        List<Task> tasks = taskRepository.findByName(name);
        if(tasks.isEmpty())
            logger.info("Task with name '"+ name +"' doesn't exist.");
        return tasks;
    }

    public List<Task> getTasksDetailsByProject(String projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty())
            logger.info("Provided project doesn't have tasks created.");
        return tasks;
    }

    public int deleteProject(String projectId) {
        boolean foundProject = getProjectById(projectId);
//        Todo: create ProjectNotFoundException
//        Todo: logic to check if project is in valid state to delete and set foundProject flag as per that
//        projectRepository.deleteById(projectId);
//        foundProject = getProjectById(projectId);
        if(foundProject){
            logger.info("Delting project with id '" + projectId + "'.");
            projectRepository.deleteById(projectId);
            return 0; // successful deletion
        } else {
            logger.info("Project with id '"+ projectId + "'not found.");
            return 1; // failed- Project not found. Please refresh Project table.
        }
        //return 2; failed- Project can not be deleted. It is in <state> state.
    }

    public int deleteTask(String taskId) {
        boolean foundTask = getTaskById(taskId);
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

    public int createProject(Project project) {
        int returnVal = 1;
        project.setProjectId(ObjectId.get());
        logger.info("Following project has been recieved from User: "+project);
        Project proj = projectRepository.save(project);
        logger.info("Following project has been saved successfully: \\n"+proj);
        //return project;
        if (proj != null)
            returnVal = 0;
        return returnVal;
    }

    public int createTask(Task task) {
        int returnVal = 1;
        task.setTaskId(ObjectId.get());
        logger.info("Following object has been obtained: "+task);
        Task savedTask = taskRepository.save(task);
        logger.info("Following project has been saved successfully: \\n"+savedTask);
        //return project;
        if (savedTask != null)
            returnVal = 0;
        return returnVal;
    }

}
