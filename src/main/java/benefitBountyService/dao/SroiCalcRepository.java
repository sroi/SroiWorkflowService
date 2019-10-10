package benefitBountyService.dao;

import benefitBountyService.models.sroi.SROICalc;

public interface SroiCalcRepository {
    SROICalc getSroiDataForProject(String projId);
}
