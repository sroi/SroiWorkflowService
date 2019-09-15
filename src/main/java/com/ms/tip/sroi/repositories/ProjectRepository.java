package com.ms.tip.sroi.repositories;

import com.ms.tip.sroi.model.Hello;
import com.ms.tip.sroi.model.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Project findBy_id(ObjectId _id);
    Project save(Project project);
}
