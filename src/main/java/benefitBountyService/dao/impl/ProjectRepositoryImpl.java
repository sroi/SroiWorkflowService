package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import benefitBountyService.mongodb.MongoDbClient;
import benefitBountyService.utils.Constants;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

public class ProjectRepositoryImpl implements ProjectRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");
    @Autowired
    private MongoTemplate mongoTemplate;

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

    @Override
    public List<Document> getProjectDetails(){


        List<Project> mongoTasks = new ArrayList<>();
        LookupOperation apprLookupOp = getLookupOperation("users", "task_info.approver", "_id", "task_info.approver_info");

        LookupOperation taskLookupOp = getLookupOperation("tasks", "_id", "projectId", "task_info");

        LookupOperation volLookupOp = getLookupOperation("users", "task_info.volunteers", "_id", "task_info.volunteer_info");

        Aggregation agg = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("projectId").ne(new ObjectId())),
                /*apprLookupOp,S
                Aggregation.unwind("$approver_info"),*/
                taskLookupOp,
                Aggregation.unwind("$task_info"),
                Aggregation.replaceRoot("task_info"),
                apprLookupOp,
                volLookupOp,
                group("_id").push(new BasicDBObject("task_info", "$task_info")
                        ).as("tasks")

         //       Aggregation.bucket("$_id")
                /*Aggregation.unwind("$volunteers"),
                volLookupOp,
                Aggregation.unwind("$volunteer_info")*/
        );


        mongoTemplate.aggregate(agg, "projects", Document.class).forEach(x -> System.out.println(x));

        return  mongoTemplate.aggregate(agg, "projects", Document.class).getMappedResults();
    }

    private LookupOperation getLookupOperation(String from , String localField, String foreignField, String as) {
        return LookupOperation.newLookup()
                .from(from)
                .localField(localField)
                .foreignField(foreignField)
                .as(as);
    }

}
