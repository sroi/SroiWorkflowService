package benefitBountyService.dao;

import benefitBountyService.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project,String> {

}
