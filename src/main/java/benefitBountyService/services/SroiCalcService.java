package benefitBountyService.services;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.sroi.SroiCalc;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

@Service
public class SroiCalcService {

    @Autowired
    private SroiCalcRepository sroiCalcRepository;

    private static final Logger logger = LoggerFactory.getLogger(SroiCalcService.class);

    public SroiCalc getSroiDataForProject(String projId) {
        if (!ObjectId.isValid(projId)) {
            String errMsg = "The project Id '" + projId + "' is not valid.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        SroiCalc calc = sroiCalcRepository.getSroiDataForProject(projId);
        Stream.of(10 <- 0).forEach(System.out :: println);
        return calc;
    }

    public SroiCalc getProjectSroiByStep(String projectId, String stepId) {
        if (!ObjectId.isValid(projectId)) {
            String errMsg = "The project Id '" + projectId + "' is not valid.";
            logger.info(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
        SroiCalc calc = sroiCalcRepository.getSroiDataForProjectByStep(projectId, stepId);
        return calc;
    }
}
