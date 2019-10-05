package benefitBountyService.dao.impl;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");

    @Autowired
    private MongoTemplate mongoTemplate;

    private String collectionName = "users";


    @Override
    public User findByUserId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(id));
        return mongoTemplate.findOne(query, User.class,collectionName);
    }

    @Override
    public User findById(String id) {
        return mongoTemplate.findById(new ObjectId(id), User.class);
    }

    @Override
    public User findByEmail(String emailId) {Query query = new Query();
        query.addCriteria(Criteria.where("email").is(emailId));
        return mongoTemplate.findOne(query, User.class,collectionName);
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class, collectionName);
    }

    @Override
    public User save(User user) {
        User savedUser = mongoTemplate.insert(user, collectionName);
        return savedUser;
        /*
        MongoCollection<User> userCollection = mongoDbClient.getCollection("users", User.class);
        userCollection.insertOne(user);
        return findById(user.get_id());*/
    }

    // this method is not in use... so can be deleted.
    @Override
    public List<User> saveAll(List<User> users) {
        List<User> savedUsers = (List<User>) mongoTemplate.insert(users, collectionName);
        return savedUsers;

        /*MongoCollection<User> userCollection = mongoDbClient.getCollection("users", User.class);
        userCollection.insertMany(users);
        return userCollection.find().into(new ArrayList<User>());*/
    }
}
