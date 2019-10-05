package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import benefitBountyService.utils.Constants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    @Override
    public List<Project> findAll(String userId, String role) {
        MongoCollection<Project> projectCollection = mongoDbClient.getCollection("projects", Project.class);
        if(role.equals(Constants.ROLES.ADMIN.name())) {
            return projectCollection.aggregate(
                    Arrays.asList(
                            Aggregates.unwind("$admin"),
                            Aggregates.match(Filters.eq("admin",userId)),
                            Aggregates.lookup("users","admin","_id", "admin_details"),
                            Aggregates.lookup("users","pointsOfContact", "_id", "pointOfContact_details")
                    )).into(new ArrayList<Project>());
        } else if (role.equals(Constants.ROLES.STAKEHOLDER.name())) {
            return projectCollection.aggregate(
                    Arrays.asList(
                            Aggregates.unwind("$stakeholder"),
                            Aggregates.match(Filters.eq("stakeholder",userId)),
                            Aggregates.lookup("users","stakeholder","_id", "stakeholder_details"),
                            Aggregates.lookup("users","pointsOfContact", "_id", "pointOfContact_details")
                    )).into(new ArrayList<Project>());
        } else {
            return new ArrayList<Project>();
        }
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
