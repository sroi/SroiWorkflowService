package benefitBountyService.dao.impl;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import benefitBountyService.services.TaskService;
import benefitBountyService.utils.Constants;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

public class TaskRepositoryImpl implements TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    @Autowired
    private MongoTemplate mongoTemplate;

    private  String collectionName = "tasks";

    @Override
    public List<Task> findByProjectId(String projectId) {

        List<Task> foundTasks = new ArrayList<>();
        List<Task> mongoTasks = new ArrayList<>();
        LookupOperation apprLookupOp = getLookupOperation("users", "approver", "_id", "approver_info");

        LookupOperation projLookupOp = getLookupOperation("projects", "projectId", "_id", "project_info");

        LookupOperation volLookupOp = getLookupOperation("users", "volunteers", "_id", "volunteer_info");

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("projectId").is(new ObjectId(projectId))),
                apprLookupOp,
                Aggregation.unwind("$approver_info"),
                projLookupOp,
                Aggregation.unwind("$project_info"),
                Aggregation.unwind("$volunteers"),
                volLookupOp,
                Aggregation.unwind("$volunteer_info")
        );

        mongoTasks = mongoTemplate.aggregate(agg, collectionName, Task.class).getMappedResults();

        foundTasks = groupingTaskByTaskId(mongoTasks);
        return foundTasks;
    }

    private List<Task> groupingTaskByTaskId(List<Task> mongoTasks) {
        List<Task> groupedTask = new ArrayList<>();
        Map<String, List<Task>> groupedTaskMap = new HashMap<>();
        if (mongoTasks.size() > 0) {
            groupedTaskMap = mongoTasks.stream().collect(Collectors.groupingBy(task -> task.getTaskId()));

            groupedTaskMap.forEach((k, v) -> createVolsInfo(v));
        }
        groupedTaskMap.forEach((k, v) -> groupedTask.add(v.get(0)));
        /*for(int pos=0; pos < mongoTasks.size(); pos += 2) {
            groupedTask.add(mongoTasks.get(pos));
        }*/
        return groupedTask;
    }

    private List<Task> createVolsInfo(List<Task> tasks) {
        Task fTask = null;
        List<User> vols = new ArrayList<>();
        tasks.forEach(task -> vols.add(task.getVolunteer_info()));
        if (tasks.size() > 0) {
            tasks.get(0).setVols_info(vols);
        }
        return tasks;
    }

    // As of now , not use-case.. so not implemented.. may be to be deleted.
    @Override
    public List<Task> findByName(String name) {
        return null;
    }

    @Override
    public Task findById(String taskId) {
        /*Document filterTask = new Document();
        filterTask.append("_id",new ObjectId(taskId));
        FindIterable<Task> taskIteration = mongoDbClient.getCollection(collectionName, Task.class).find(filterTask,Task.class);
        return taskIteration.first();*/
        return mongoTemplate.findById(new ObjectId(taskId), Task.class);
    }

    @Override
    public long deleteById(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(taskId)));
        long deletedCount = mongoTemplate.remove(query, Task.class).getDeletedCount();
        return deletedCount;
    }

    @Override
    public Task save(Task task) {
        return null;
    }

    public Task fetchByTaskId(String taskId){
        Task foundTask = null;
        LookupOperation apprLookupOp = getLookupOperation("users", "approver", "_id", "approver_info");

        LookupOperation projLookupOp = getLookupOperation("projects", "projectId", "_id", "project_info");

        LookupOperation volLookupOp = getLookupOperation("users", "volunteers", "_id", "volunteer_info");

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(new ObjectId(taskId))),
                apprLookupOp,
                Aggregation.unwind("$approver_info"),
                projLookupOp,
                Aggregation.unwind("$project_info"),
                Aggregation.unwind("$volunteers"),
                volLookupOp,
                Aggregation.unwind("$volunteer_info")
        );

        List<Task> tasks = mongoTemplate.aggregate(agg, collectionName, Task.class).getMappedResults();
        List<User> vols = new ArrayList<>();
        tasks.forEach(task -> vols.add(task.getVolunteer_info()));
        /*List<User> vol2 = new ArrayList<>();
        tasks.stream().map(task -> vol2.add(task.getVolunteer_info()));*/
        if (tasks.size() > 0) {
            tasks.get(0).setVols_info(vols);
            foundTask = tasks.get(0);
        }
        return foundTask;
    }

    private LookupOperation getLookupOperation(String from , String localField, String foreignField, String as) {
        return LookupOperation.newLookup()
                .from(from)
                .localField(localField)
                .foreignField(foreignField)
                .as(as);
    }

    @Override
    public List<Task> getTasksPerRoles(String userId, String role) {
        List<Task> foundTasks = new ArrayList<>();
        List<Task> mongoTasks = new ArrayList<>();
        LookupOperation apprLookupOp = getLookupOperation("users", "approver", "_id", "approver_info");

        LookupOperation projLookupOp = getLookupOperation("projects", "projectId", "_id", "project_info");

        LookupOperation volLookupOp = getLookupOperation("users", "volunteers", "_id", "volunteer_info");

        Aggregation agg = null;
        if (role.equalsIgnoreCase(Constants.ROLES.APPROVER.name())) {
            logger.info("Finding tasks for User '" + userId + "' against Approver role");
            agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("approver").is(new ObjectId(userId))),
                    apprLookupOp,
                    Aggregation.unwind("$approver_info"),
                    projLookupOp,
                    Aggregation.unwind("$project_info"),
                    Aggregation.unwind("$volunteers"),
                    volLookupOp,
                    Aggregation.unwind("$volunteer_info")
            );
        } else if (role.equalsIgnoreCase(Constants.ROLES.VOLUNTEER.name())) {
            logger.info("Finding tasks for User '" + userId + "' against Volunteer role");
            agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("volunteers").all(new ObjectId(userId))),
                    apprLookupOp,
                    Aggregation.unwind("$approver_info"),
                    projLookupOp,
                    Aggregation.unwind("$project_info"),
                    Aggregation.unwind("$volunteers"),
                    volLookupOp,
                    Aggregation.unwind("$volunteer_info")
            );
        } else {
            String errMsg = role + " is incorrect role to see tasks";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }

        mongoTasks = mongoTemplate.aggregate(agg, collectionName, Task.class).getMappedResults();

        foundTasks = groupingTaskByTaskId(mongoTasks);
        return foundTasks;
    }

    // Temporary method to verify lookup operation in java.. to be deleted
    public void fetchByUserIdT(String taskId){
        MongoCollection<Document> taskCollection = mongoDbClient.getCollection("tasks");//, Task.class);
        MongoCollection<Task> tempTaskCol = mongoDbClient.getCollection("tasks", Task.class);//, Task.class);

        List<Task> tasks = new ArrayList<>();

//        BsonDocument match =  new BsonDocument("$match" , Filters.eq("_id", new BsonObjectId(taskId)));
        BsonDocument apprDoc = new BsonDocument("$lookup", new BsonDocument("from", new BsonString("users"))
                .append("localField", new BsonString("approver"))
                .append("foreignField", new BsonString("_id"))
                .append("as", new BsonString("approver_details")));
        BsonDocument apprUW = new BsonDocument("$unwind", new BsonString("$approver_details"));
        System.out.println("Printing command : " +apprDoc + "  , " + apprUW);

        BsonDocument volUW = new BsonDocument("$unwind", new BsonString("$volunteers"));

        BsonDocument volDoc = new BsonDocument("$lookup", new BsonDocument("from", new BsonString("users"))
                .append("localField", new BsonString("volunteers"))
                .append("foreignField", new BsonString("_id"))
                .append("as", new BsonString("vol_details")));
        BsonDocument voluwDoc = new BsonDocument("$unwind", new BsonString("$vol_details"));
        BsonDocument volPush = new BsonDocument("$push", new BsonString("$vol_details"));
        System.out.println("Printing command : " + volUW + "  ,  " + volDoc + "  ,  " + voluwDoc);

        BsonDocument volGrp = new BsonDocument("$group", new BsonDocument("_id", new BsonString("$_id")).
                append("name", new BsonDocument("$first", new BsonString("$name"))).
                append("desc", new BsonDocument("$first", new BsonString("$description"))).
                append("vols_info", volPush));

        System.out.println("Printing command : " + volGrp);

        AggregateIterable<Document> output2 = taskCollection.aggregate(
                Arrays.asList(Aggregates.match(Filters.eq("_id",new ObjectId(taskId))),apprDoc,apprUW, volUW, volDoc, voluwDoc, volGrp)
        );

//        System.out.println(apprDoc.);
        System.out.println("Printing output2 string ***" +output2);

        for (Document out : output2) {
            System.out.println("doc printing output2 ....... " + out);
        }
//            System.out.println("Printing output2 : " +output2.first());
        //        BsonDocument finalDoc = new BsonDocument(apprDoc).apprDoc.(doc2);

        BsonDocument taskGrp = new BsonDocument("$group", new BsonDocument("_id", new BsonString("$_id")).
                append("name", new BsonDocument("$first", new BsonString("$name"))).
                append("desc", new BsonDocument("$first", new BsonString("$description"))).
                append("vols_info", new BsonDocument("$push", new BsonString("$volunteer_info"))));

        AggregateIterable<Document> output = taskCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("_id",new ObjectId(taskId))),
//                        Aggregates.unwind("$approver"),
//                        Aggregates.
//                        doc
                        Aggregates.lookup("users","approver", "_id", "approver_info"),
                        Aggregates.unwind("$approver_info"),
                        Aggregates.lookup("projects","projectId", "_id", "project_info"),
                        Aggregates.unwind("$project_info"),
                        Aggregates.unwind("$volunteers"),
                        Aggregates.lookup("users","volunteers", "_id", "volunteer_info"),
                        Aggregates.unwind("$volunteer_info"),
//                        taskGrp
                        Aggregates.group("$_id",Accumulators.push("vols_info","$volunteer_info"))
//Projections.include("_id", "name", "description","vols_info")
//                        Aggregates.unwind("$approver")
                )
        );
        for (Document out : output) {
            System.out.println("doc printing ....... "+out);
        }

        ArrayList<Task> taskCol = tempTaskCol.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("_id",new ObjectId(taskId))),
                        Aggregates.lookup("users","approver", "_id", "approver_info"),
                        Aggregates.unwind("$approver_info"),
                        Aggregates.lookup("projects","projectId", "_id", "project_info"),
                        Aggregates.unwind("$project_info"),
                        Aggregates.unwind("$volunteers"),
                        Aggregates.lookup("users","volunteers", "_id", "volunteer_info"),
                        Aggregates.unwind("$volunteer_info"),
//                        taskGrp
                        Aggregates.group("$_id",Accumulators.push("vols_info","$volunteer_info"))
//Projections.include("_id", "name", "description","vols_info")
                )
        ).into(new ArrayList<Task>());

        LookupOperation apprLookupOp = LookupOperation.newLookup()
                .from("users")
                .localField("approver")
                .foreignField("_id")
                .as("approver_info");

        LookupOperation projLookupOp = LookupOperation.newLookup()
                .from("projects")
                .localField("projectId")
                .foreignField("_id")
                .as("project_info");

        LookupOperation volLookupOp = LookupOperation.newLookup()
                .from("users")
                .localField("volunteers")
                .foreignField("_id")
                .as("volunteer_info");

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(new ObjectId(taskId))),
                apprLookupOp,
                Aggregation.unwind("$approver_info"),
                projLookupOp,
                Aggregation.unwind("$project_info"),
                Aggregation.unwind("$volunteers"),
                volLookupOp,
                Aggregation.unwind("$volunteer_info")

//                Accumulators.push("vols_info","$volunteer_info"),
//                Aggregates.group("$_id",Accumulators.push("vols_info","$volunteer_info"))
//                Aggregation.group("_id", "name", "description", "$volunteer_info", GroupOperation(Aggregation.("vols_info","$volunteer_info"))
//                Projections.computed("vols_info", )
        );

        List<Task> tasks1 = mongoTemplate.aggregate(agg, "tasks", Task.class).getMappedResults();
        System.out.println("==================");
        tasks1.forEach(System.out::println);
        System.out.println("********* tempTask results are " + tasks1.get(0));
//        return tasks.get(0);
    }

}
