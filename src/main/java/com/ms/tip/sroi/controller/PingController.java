package com.ms.tip.sroi.controller;

import com.ms.tip.sroi.model.Hello;
import com.ms.tip.sroi.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ping")
public class PingController {
    @Autowired
    private TestRepository repository;

    @RequestMapping("/status")
    public String ping() {
        List<Hello> list = repository.findAll();
        list.forEach(h -> System.out.println(h));
        if (!list.isEmpty()) {
            return "Your application is up and runnng..!!";
        } else
            return "Application is down";
    }
}
