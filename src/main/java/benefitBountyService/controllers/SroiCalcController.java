package benefitBountyService.controllers;

import benefitBountyService.models.sroi.SroiCalc;
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
    public SroiCalc getSROIData(@RequestParam("pid") String projectId) {
        SroiCalc calc = sroiCalcService.getSroiDataForProject(projectId);
        return calc;
    }

    @GetMapping(value = "/bystep")
    public SroiCalc getSROIDataByStep(@RequestParam("pid") String projectId, @RequestParam("step") String stepId) {
        SroiCalc calc = sroiCalcService.getProjectSroiByStep(projectId, stepId);
        return calc;
    }
}
