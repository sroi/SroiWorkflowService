package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import benefitBountyService.utils.Constants;
import benefitBountyService.utils.MongoDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import java.util.List;


public class ProjectRepositoryImpl implements ProjectRepository {

    private final String collectionName = "projects";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Project> findAll(String userId, String role) {
        List<Project> projects = null;
        Aggregation agg = null;

        if(Constants.ROLES.ADMIN.name().equals(role)) {
            agg = Aggregation.newAggregation(
                    MongoDbUtils.getLookupOperation("users", "created_by", "_id", "admin"),
                    Aggregation.unwind("$admin")
                    );
            projects = mongoTemplate.aggregate(agg, collectionName, Project.class).getMappedResults();
        } else if (Constants.ROLES.STAKEHOLDER.name().equals(role)) {
            agg = Aggregation.newAggregation(
                    Aggregation.unwind("stakeholder"),
                    MongoDbUtils.getLookupOperation("users", "stakeholder", "_id", "stakeholder_info")
            );
            projects = mongoTemplate.aggregate(agg, collectionName, Project.class).getMappedResults();

        } //@TODO : Yet to add condition if ROLE is Volunteer or Approver

        return projects;
    }

    @Override
    public Project findById(String projectId) {
        return null;
    }

    @Override
    public void deleteById(String projectId) {

    }

    @Override
    public Project save(Project prj) {
        return null;
    }
}
