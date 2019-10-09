package benefitBountyService.dao;

import benefitBountyService.models.Project;
import benefitBountyService.models.User;

import java.util.List;

public interface ProjectRepository {

    List<Project> findAll(String userId, String role);

    Project findById(String projectId);

    long deleteById(String projectId);

    Project save(Project prj);

    List<Project> getAllProjects();

    int changeTaskStatus(User user, Project project, String role, String comment);

}
