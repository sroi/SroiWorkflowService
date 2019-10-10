package benefitBountyService.dao.impl;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.sroi.SROICalc;
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
    public SROICalc getSroiDataForProject(String projId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("proj_id").is(new ObjectId(projId)));
        SROICalc calc = mongoTemplate.find(query, SROICalc.class, collectionName).get(0);
        return calc;
    }
}
