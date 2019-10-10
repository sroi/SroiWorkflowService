package benefitBountyService.services;

import benefitBountyService.dao.SroiCalcRepository;
import benefitBountyService.models.sroi.SROICalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SroiCalcService {

    @Autowired
    private SroiCalcRepository sroiCalcRepository;

    private static final Logger logger = LoggerFactory.getLogger(SroiCalcService.class);

    public SROICalc getProjects(String projId) {
        SROICalc calc = sroiCalcRepository.getSroiDataForProject(projId);
        Stream.of(10 <- 0).forEach(System.out :: println);
        return calc;
    }
}
