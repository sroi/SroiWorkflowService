package benefitBountyService.dao.impl;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.sroi.SroiCalc;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class SroiCalcRepositoryImpl implements SroiCalcRepository {

    private String collectionName = "sroi_data";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public SroiCalc getSroiDataForProject(String projId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("proj_id").is(new ObjectId(projId)));
        SroiCalc calc = mongoTemplate.find(query, SroiCalc.class, collectionName).get(0);
        return calc;
    }

    @Override
    public SroiCalc getSroiDataForProjectByStep(String projectId, String stepId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("proj_id").is(new ObjectId(projectId)));
        return null;
    }
}
