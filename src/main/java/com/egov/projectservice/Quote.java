package com.egov.projectservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "quotes")
public class Quote{

    @Id
    String id;
    String contractorId;
    String projectId;
    String description;
    Integer price;
    List<String> messages;
}
