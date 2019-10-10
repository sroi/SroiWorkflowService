package benefitBountyService.models;

import java.util.List;

public class ProjectDetails {
    private Project project;
    private List<Task> tasks;

    public ProjectDetails(Project project, List<Task> tasks) {
        this.project = project;
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
