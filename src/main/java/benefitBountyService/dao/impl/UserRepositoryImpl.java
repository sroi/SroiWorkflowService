package benefitBountyService.dao.impl;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.mongodb.MongoDbClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    MongoDbClient mongoDbClient = new MongoDbClient("sroi");


    @Override
    public User findByUserId(String id) {
        Document filterUser = new Document();
        filterUser.append("user_id",id);
        FindIterable<User> userIteration = mongoDbClient.getCollection("users", User.class).find(filterUser,User.class);
        return userIteration.first();
    }

    @Override
    public User findById(String id) {
        /*
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", new ObjectId(id));
        FindIterable<User> userIter = mongoDbClient.getCollection("users", User.class).find(whereQuery, User.class);
        User newUser = userIter.first();
        */
        Document filterUser = new Document();
        filterUser.append("_id",new ObjectId(id));
        FindIterable<User> userIteration = mongoDbClient.getCollection("users", User.class).find(filterUser,User.class);
        User user = userIteration.first();
        return user;
    }

    @Override
    public User findByEmail(String emailId) {
        Document filterUser = new Document();
        filterUser.append("email",emailId);
        FindIterable<User> userIteration = mongoDbClient.getCollection("users", User.class).find(filterUser,User.class);
        return userIteration.first();
    }

    @Override
    public List<User> findAll() {
        MongoCollection<User> userCollection = mongoDbClient.getCollection("users", User.class);
        return userCollection.find().into(new ArrayList<User>());
    }

    @Override
    public User save(User user) {
        MongoCollection<User> userCollection = mongoDbClient.getCollection("users", User.class);
        userCollection.insertOne(user);
        return findById(user.get_id());
    }

    @Override
    public List<User> saveAll(List<User> users) {
        MongoCollection<User> userCollection = mongoDbClient.getCollection("users", User.class);
        userCollection.insertMany(users);
        return userCollection.find().into(new ArrayList<User>());
    }
}
