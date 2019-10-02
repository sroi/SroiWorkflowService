package benefitBountyService.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDbClient {

    private final MongoClient mongoClient = new MongoClient();
    private final MongoDatabase mongoDatabase;


    public MongoDbClient(String databaseName){
        this.mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public <className> MongoCollection<className> getCollection(String collectionName, Class className){
        return this.mongoDatabase.getCollection(collectionName, className);
    }

}
