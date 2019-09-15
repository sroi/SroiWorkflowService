package com.ms.tip.sroi.repositories;

import com.ms.tip.sroi.model.Hello;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<Hello, String> {

    Hello findBy_id(ObjectId _id);
}
