package benefitBountyService.dao.impl;

import benefitBountyService.dao.FileRepository;
import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.File;
import benefitBountyService.models.Project;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import benefitBountyService.utils.Constants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileRepositoryImpl implements FileRepository {

    //MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    String collectionName = "files";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<File> findAllByTaskId(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("taskId").is(taskId));
        return mongoTemplate.find(query, File.class,collectionName);
    }

    @Override
    public File findById(String fileId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(fileId));
        return mongoTemplate.findOne(query, File.class,collectionName);
    }

    @Override
    public long deleteById(String fileId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(fileId)));
        long deletedCount = mongoTemplate.remove(query, File.class).getDeletedCount();
        return deletedCount;
    }

    @Override
    public void insert(File file) {
        File savedFile = mongoTemplate.insert(file, collectionName);
    }

}
