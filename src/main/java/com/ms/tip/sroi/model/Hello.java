package com.ms.tip.sroi.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Hello {
    @Id
    public ObjectId _id;
    public String name;
    public Integer age;
    public String comapny;

    // Constructors
    public Hello() {}
    public Hello(ObjectId _id, String name, Integer age, String company) {
        this._id = _id;
        this.name = name;
        this.age = age;
        this.comapny = company;
    }

    // ObjectId needs to be converted to string
    public String get_id() {
        return _id.toHexString();
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCompany() {
        return comapny;
    }

    public void setCompany(String company) {
        this.comapny = company;
    }

    @Override
    public String toString(){
        return "Name: "+ name + " and Age: " +age + " and Company:" + comapny;
    }
}
