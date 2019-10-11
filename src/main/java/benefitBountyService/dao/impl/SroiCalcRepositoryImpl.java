package benefitBountyService.dao.impl;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.sroi.SroiCalc;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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
        String stepNo = "stepOne";
        switch (Integer.parseInt(stepId)) {
            case 1: break;
            case 2: stepNo = "stepTwo"; break;
            case 3: stepNo = "stepThree"; break;
            default: break;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("proj_id").is(new ObjectId(projectId)));
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("proj_id").is(new ObjectId(projectId))),
                Aggregation.project("proj_id", "user_id", stepNo)
                );

        SroiCalc mongoDoc = mongoTemplate.aggregate(agg, collectionName, SroiCalc.class).getUniqueMappedResult();
        return mongoDoc;
    }

    @Override
    public SroiCalc saveProjectSroiData(SroiCalc sroiCalc, String stepId) {
        SroiCalc savedSroi = mongoTemplate.save(sroiCalc, collectionName);
        return savedSroi;
    }

    public enum Steps {
        ONE(1), TWO(2);

        private int stepId;
        private Steps (int stepId) {
            this.stepId = stepId;
        }

        public int getStepId() {
            return stepId;
        }

    }
}
