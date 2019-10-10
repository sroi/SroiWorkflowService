package benefitBountyService.dao;

import benefitBountyService.models.Project;
import org.bson.Document;

import java.lang.annotation.Documented;
import java.util.List;

public interface ProjectRepository {

    List<Project> findAll(String userId, String role);

    Project findById(String projectId);

    void deleteById(String projectId);

    Project save(Project prj);

    List<Document> getProjectDetails();
}
