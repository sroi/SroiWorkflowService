package benefitBountyService.mongodb;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoSpringClient extends AbstractMongoConfiguration {

    private final String connectionString = "mongodb+srv://sroi:sroi@clustersroi-9shtc.mongodb.net/sroi?retryWrites=true&w=majority";

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(connectionString));
    }

    @Override
    protected String getDatabaseName() {
        return "sroi";
    }
}
