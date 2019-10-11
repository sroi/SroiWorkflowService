package benefitBountyService.services;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.User;
import benefitBountyService.models.sroi.SroiCalc;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
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

    public SroiCalc saveProjectSroiData(SroiCalc sroiCalc, String stepId) {
        SroiCalc saved = null;
        //To get it from session
        User loggedInUser = new User();

        SroiCalc calc = sroiCalcRepository.getSroiDataForProjectByStep(sroiCalc.getProjectId(), stepId);
        if (calc != null) {

        } else {
            saved = createProjectSroiData(sroiCalc, stepId);
        }
        return saved;
    }

    public SroiCalc createProjectSroiData(SroiCalc sroiCalc, String stepId) {
        //To get it from session
        User loggedInUser = new User();
        sroiCalc.setCreated_by(loggedInUser.getUserId());
        sroiCalc.setCreated_on(new Date());
        sroiCalc.setUpdated_by(loggedInUser.getUserId());
        sroiCalc.setUpdated_on(new Date());

        SroiCalc savedSroi = sroiCalcRepository.saveProjectSroiData(sroiCalc, stepId);
        return savedSroi;
    }
}
