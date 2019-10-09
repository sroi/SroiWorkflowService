package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.utils.Constants;
import benefitBountyService.utils.MongoDbUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;


public class ProjectRepositoryImpl implements ProjectRepository {

    private final String collectionName = "projects";

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Project> findAll(String userId, String role) {
        List<Project> projects = new ArrayList<>();
        Aggregation agg = null;
        if(Constants.ROLES.ADMIN.name().equalsIgnoreCase(role)) {
            agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("created_by").is(userId)),
                    MongoDbUtils.getLookupOperation("users", "created_by", "user_id", "admin"),
                    Aggregation.unwind("admin")
            );
            projects = mongoTemplate.aggregate(agg, collectionName, Project.class).getMappedResults();
        } else if (Constants.ROLES.STAKEHOLDER.name().equalsIgnoreCase(role)) {
            Query stakeholderIdQuery = new Query().addCriteria(Criteria.where("user_id").is(userId));
            String stakeholderUserId  = mongoTemplate.find(stakeholderIdQuery, User.class,"users").get(0).get_id();
            agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("stakeholder").is(new ObjectId(stakeholderUserId))),
                    MongoDbUtils.getLookupOperation("users", "stakeholder", "_id", "stakeholderList")
            );
            projects = mongoTemplate.aggregate(agg, collectionName, Project.class).getMappedResults();

        } //@TODO : Yet to add condition if ROLE is Volunteer or Approver

        return projects;
    }

    @Override
    public Project findById(String projectId) {
        return mongoTemplate.findById(new ObjectId(projectId), Project.class);
    }

    @Override
    public long deleteById(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(projectId)));
        return mongoTemplate.remove(query, collectionName).getDeletedCount();
    }

    @Override
    public Project save(Project prj) {
        return mongoTemplate.save(prj, collectionName);
    }

    @Override
    public List<Project> getAllProjects() {
        return mongoTemplate.findAll(Project.class, collectionName);
    }
}
