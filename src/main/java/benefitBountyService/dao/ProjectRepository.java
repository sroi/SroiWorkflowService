package benefitBountyService.dao;

import benefitBountyService.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    List<Project> findAll(String userId, String role);

    Project findById(String projectId);

    void deleteById(String projectId);

    Project save(Project prj);
}
