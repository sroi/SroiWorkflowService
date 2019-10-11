package benefitBountyService.dao;

import benefitBountyService.models.sroi.SroiCalc;
import benefitBountyService.models.sroi.StepOne;

public interface SroiCalcRepository {
    SroiCalc getSroiDataForProject(String projId);

    SroiCalc getSroiDataForProjectByStep(String projectId, String stepId);

    SroiCalc saveProjectSroiData(SroiCalc sroiCalc, String stepId);

    StepOne saveStepOneData(StepOne one);
}
