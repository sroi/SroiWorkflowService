package benefitBountyService.dao;

import benefitBountyService.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository {

    List<Project> findAll();

}
