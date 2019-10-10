package benefitBountyService.controllers;

import benefitBountyService.models.sroi.SROICalc;
import benefitBountyService.services.SroiCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sroi")
public class SroiCalcController {

    @Autowired
    private SroiCalcService sroiCalcService;

    @GetMapping(value = "/all")
    public SROICalc getSROIData(@RequestParam("pid") String projectId) {
        SROICalc calc = sroiCalcService.getProjects(projectId);
        return calc;
    }
}
