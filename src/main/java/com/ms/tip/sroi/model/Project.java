package com.ms.tip.sroi.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.annotation.Generated;
import java.util.Objects;

public class Project {

    @Id
    public ObjectId _id;
    private String projectID;
    private String name;
    private String code;
    private String engageArea;
    private String summary;
    private String duration;
    private String budget;
    private String company;
    private String location;
    private String emailofPOC;

    // ObjectId needs to be converted to string
    public String get_id() {
        return _id.toHexString();
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEngageArea() {
        return engageArea;
    }

    public void setEngageArea(String engageArea) {
        this.engageArea = engageArea;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loation) {
        this.location = loation;
    }

    public String getEmailofPOC() {
        return emailofPOC;
    }

    public void setEmailofPOC(String emailofPOC) {
        this.emailofPOC = emailofPOC;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID='" + projectID + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", engageArea='" + engageArea + '\'' +
                ", summary='" + summary + '\'' +
                ", duration='" + duration + '\'' +
                ", budget='" + budget + '\'' +
                ", company='" + company + '\'' +
                ", loation='" + location + '\'' +
                ", emailofPOC='" + emailofPOC + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(projectID, project.projectID) &&
                Objects.equals(name, project.name) &&
                Objects.equals(code, project.code) &&
                Objects.equals(engageArea, project.engageArea) &&
                Objects.equals(summary, project.summary) &&
                Objects.equals(duration, project.duration) &&
                Objects.equals(budget, project.budget) &&
                Objects.equals(company, project.company) &&
                Objects.equals(location, project.location) &&
                Objects.equals(emailofPOC, project.emailofPOC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID);
    }
}
