package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Aggregates.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    @Override
    public List<Project> findAll(String userId, String role) {
        MongoCollection<Project> projectCollection = mongoDbClient.getCollection("projects", Project.class);
        //List<DBObject> unwindItems = new ArrayList<>();
        if(role.equals(User.Roles.Admin.name())) {

        } else if (role.equals(User.Roles.Stakeholder.name())) {
            filter.append("stakeholder",userId);
        }

        DBObject a = new BasicDBObject()

        //MongoCollection<Project> projectCollection = mongoDbClient.getCollection("projects", Project.class);
        //return projectCollection.find().into(new ArrayList<Project>());
    }
}
