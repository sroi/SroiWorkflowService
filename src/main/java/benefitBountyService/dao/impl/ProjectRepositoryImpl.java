package benefitBountyService.dao.impl;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Activity;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.services.ProjectService;
import benefitBountyService.utils.Constants;
import benefitBountyService.utils.MongoDbUtils;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProjectRepositoryImpl implements ProjectRepository {

    private final String collectionName = "projects";

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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
                    MongoDbUtils.getLookupOperation("users", "stakeholder", "_id", "stakeholderList"),
                    MongoDbUtils.getLookupOperation("users", "pointOfContacts", "_id", "pointOfContact"),
                    Aggregation.unwind("$pointOfContact")
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
        Project savedPrj = mongoTemplate.save(prj, collectionName);
        if (savedPrj != null) {
            logger.info("Project saved successfully. Project Details: " + savedPrj);
        } else {

        }
        return savedPrj;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = mongoTemplate.findAll(Project.class, collectionName);
        if (projects.isEmpty()) {
            logger.info("Projects found. Count is " +projects.size());
        }
        return projects;
    }

    @Override
    public int changeTaskStatus(User loggedInUser, Project project, String role, String comment) {
        int updated = -1;
        Project savedPrj = null;
        if (role.equalsIgnoreCase(Constants.ROLES.STAKEHOLDER.toString())) {
            updated = changeStatusForStakeholder(loggedInUser, project, comment);
        } else {
            updated = changeStatusForAdmin(loggedInUser, project, comment);
        }

//        saveProjectActivity(savedPrj);
        return updated;
    }

    private int changeStatusForAdmin(User loggedInUser, Project project, String comment) {
        int updated = -1;
        logger.info("Updating or creating Project entry for Admin");
        Project savedPrj = save(project);

        //Updating or creating Activity entry for Approver
        logger.info("Updating or creating Activity entry for Admin");
        Query actQuery = new Query();
        actQuery.addCriteria(Criteria.where("projectId").is(new ObjectId(project.getProjectId()))
                .andOperator(Criteria.where("role").is(Constants.ROLES.ADMIN.toString())
                        .andOperator(Criteria.where("userId").is(new ObjectId(loggedInUser.get_id())))));

        Activity activity = mongoTemplate.findOne(actQuery, Activity.class);
        String activityStr = "Project has been " + project.getStatus().toLowerCase() + " by Admin";
        if (activity != null) {
            activity.setComments(comment);
            activity.setUpdatedBy(loggedInUser.getUserId());
            activity.setUpdatedOn(new Date());
            activity.setActivity(activityStr);
        } else {

            activity = new Activity(ObjectId.get(), new ObjectId(project.getProjectId()), null, new ObjectId(loggedInUser.get_id()), loggedInUser.getName(), Constants.ROLES.ADMIN.toString(),
                    activityStr, comment, loggedInUser.getUserId(), new Date(), loggedInUser.getUserId(), new Date());
        }

        Activity act = mongoTemplate.save(activity);

        if (savedPrj != null && act != null) {
            updated = 0;
        }
        return updated;
    }

    private int changeStatusForStakeholder(User loggedInUser, Project project, String comment) {
        int updated = -1;

        logger.info("Updating or creating Project entry for Stakeholder");
        Project savedPrj = save(project);
//        Task upTask = updateTaskStatus(loggedInUser, task, status);

        //Updating or creating Activity entry for Approver
        logger.info("Updating or creating Activity entry for Stakeholder");
        Query actQuery = new Query();
        actQuery.addCriteria(Criteria.where("projectId").is(new ObjectId(project.getProjectId()))
                .andOperator(Criteria.where("role").is(Constants.ROLES.STAKEHOLDER.toString())
                        .andOperator(Criteria.where("userId").is(new ObjectId(loggedInUser.get_id())))));

        Activity activity = mongoTemplate.findOne(actQuery, Activity.class);
        String activityStr = "Task has been " + project.getStatus().toLowerCase() + " by Stakeholder";
        if (activity != null) {
            activity.setComments(comment);
            activity.setUpdatedBy(loggedInUser.getUserId());
            activity.setUpdatedOn(new Date());
            activity.setActivity(activityStr);
        } else {

            activity = new Activity(ObjectId.get(), new ObjectId(project.getProjectId()), null, new ObjectId(loggedInUser.get_id()), loggedInUser.getName(), Constants.ROLES.STAKEHOLDER.toString(),
                    activityStr, comment, loggedInUser.getUserId(), new Date(), loggedInUser.getUserId(), new Date());
        }

        Activity act = mongoTemplate.save(activity);

        if (savedPrj != null && act != null) {
            updated = 0;
        }
        return updated;
    }

    @Override
    public List<Document> getProjectDetails(String status){



        List<Project> mongoTasks = new ArrayList<>();
        LookupOperation apprLookupOp = MongoDbUtils.getLookupOperation("users", "task_info.approver", "_id", "task_info.approver_info");

        LookupOperation taskLookupOp = MongoDbUtils.getLookupOperation("tasks", "_id", "projectId", "task_info");

        LookupOperation volLookupOp = MongoDbUtils.getLookupOperation("users", "task_info.volunteers", "_id", "task_info.volunteer_info");
        LookupOperation sthLookupOp = MongoDbUtils.getLookupOperation("users", "stakeholder", "_id", "task_info.stakeholder_info");

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is(status)),
              // Aggregation.match(Criteria.where("startDate").gte(java.util.Calendar.getInstance().)),//startDate
              //  Aggregation.match(Criteria.where("endDate").lte(new Date("2019-10-30T18:30:00.000+0000"))),//endDate
                /*apprLookupOp,S
                Aggregation.unwind("$approver_info"),*/
                taskLookupOp,
                Aggregation.unwind("$task_info"),
//                Aggregation.replaceRoot().withValueOf(ObjectOperators.valueOf("task_info").mergeWith(Aggregation.ROOT)),
//                Aggregation.replaceRoot().withValueOf(Aggregation.ROOT),
                apprLookupOp,
                Aggregation.unwind("$task_info.approver_info"),
                volLookupOp,
                sthLookupOp,
                Aggregation.group("_id","name","areaOfEngagement","startDate","endDate","budget","corporate","location").push(new BasicDBObject("task_info", "$task_info")
                ).as("tasks")

                //       Aggregation.bucket("$_id")
                /*Aggregation.unwind("$volunteers"),
                volLookupOp,
                Aggregation.unwind("$volunteer_info")*/
        );

        List<Document> mongoDocs = mongoTemplate.aggregate(agg, "projects", Document.class).getMappedResults();

        return mongoDocs;
    }
}
