package benefitBountyService.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoDbClient {

    //private String connectionString = "mongodb+srv://sroi:sroi@clustersroi-9shtc.mongodb.net/sroi?retryWrites=true&w=majority";
    private final MongoClient mongoClient = new MongoClient();
    private final MongoDatabase mongoDatabase;
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().
            conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()));
//    MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build();

    public MongoDbClient(String databaseName){
        this.mongoDatabase = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
    }

    public <className> MongoCollection<className> getCollection(String collectionName, Class className){
        return this.mongoDatabase.getCollection(collectionName, className);
    }

    public MongoCollection<Document> getCollection(String collectionName){
        return this.mongoDatabase.getCollection(collectionName);
    }

}
