package benefitBountyService.dao.impl;

import benefitBountyService.dao.TaskRepository;
import benefitBountyService.models.Project;
import benefitBountyService.models.Task;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.NewTaskTO;
import benefitBountyService.models.dtos.TaskTO;
import benefitBountyService.models.dtos.TempTO;
import benefitBountyService.mongodb.MongoDbClient;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    @Autowired
    private MongoTemplate mongoTemplate;

    private  String collectionName = "tasks";

    @Override
    public List<Task> findByProjectId(String projectId) {
        return null;
    }

    @Override
    public List<Task> findByName(String name) {
        return null;
    }

    @Override
    public Task findById(String taskId) {
        Document filterTask = new Document();
        filterTask.append("_id",new ObjectId(taskId));
        FindIterable<Task> taskIteration = mongoDbClient.getCollection(collectionName, Task.class).find(filterTask,Task.class);
        return taskIteration.first();
    }

    @Override
    public void deleteById(String taskId) {

    }

    @Override
    public Task save(Task task) {
        return null;
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    public Task fetchByUserId(String taskId){
        MongoCollection<Task> taskCollection = mongoDbClient.getCollection("tasks", Task.class);

//        List<Task> tasks = new ArrayList<>();
        /*List<Task> tasks = taskCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("_id",new ObjectId(taskId))),
                        Aggregates.unwind("$approver"),
                        Aggregates.lookup("users","approver","_id", "approver_details")
                    )
                ).into(new ArrayList<Task>());*/
//        ).forEach(task -> System.out.p);


        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("users")
                .localField("approver")
                .foreignField("_id")
                .as("approver_info");

        LookupOperation projLookupOp = LookupOperation.newLookup()
                .from("projects")
                .localField("projectId")
                .foreignField("_id")
                .as("proj_info");

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(new ObjectId(taskId))),
                lookupOperation,
                projLookupOp
        );

        ProjectionOperation projStage = Aggregation.project("name", "approver","approver_info");

        Aggregation tempAgg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(new ObjectId(taskId))),
                lookupOperation,
                projStage
        );
        NewTaskTO task = mongoTemplate.aggregate(agg, "tasks", NewTaskTO.class).getUniqueMappedResult();

        System.out.println("********* task results are " + task);
        TempTO tempTask = mongoTemplate.aggregate(tempAgg, "tasks", TempTO.class).getUniqueMappedResult();
        /*mongoDbClient.getCollection(collectionName, Task.class).
                aggregate(Arrays.asList(Aggregates.unwind("approver")));*/
        //return tasks.get(0);

        System.out.println("********* tempTask results are " + tempTask);
        return new Task();
    }

    public void fetchByUserIdT(String taskId){
        MongoCollection<Document> taskCollection = mongoDbClient.getCollection("tasks");//, Task.class);

        List<Task> tasks = new ArrayList<>();

        BsonDocument doc = new BsonDocument("$lookup", new BsonDocument("from", new BsonString("users"))
                .append("localField", new BsonString("approver"))
                .append("foreignField", new BsonString("_id"))
                .append("as", new BsonString("approver_details")));

        AggregateIterable<Document> output = taskCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("_id",new ObjectId(taskId))),
//                        Aggregates.unwind("$approver"),
//                        Aggregates.
//                        doc
                        Aggregates.lookup("users","approver", "_id", "approver_details")
                )
        );

        for (Document out : output) {
            System.out.println("doc printing ....... "+out);
        }
//        return tasks.get(0);
    }

}
