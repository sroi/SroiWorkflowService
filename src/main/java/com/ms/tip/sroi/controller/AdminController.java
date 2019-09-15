package com.ms.tip.sroi.controller;

import com.ms.tip.sroi.model.Project;
import com.ms.tip.sroi.repositories.ProjectRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/createPr", method = RequestMethod.POST)

    public String addProject(@RequestBody Project project){
        project.set_id(ObjectId.get());
        System.out.println("Following object has been obtained: "+project);
        projectRepository.save(project);
        System.out.println("Following project has been saved successfully: \\n"+project);
        //return project;
        return "Project Saved";
    }
}
