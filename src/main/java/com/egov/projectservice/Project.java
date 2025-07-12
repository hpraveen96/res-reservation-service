package com.egov.projectservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "projects")
public class Project {

    @Id
    String id;
    String phone;
    String name;
    String description;
    String location;
    String startDate;
    String status;
    double budget;
}
