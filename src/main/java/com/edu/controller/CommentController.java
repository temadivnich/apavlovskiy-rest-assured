package com.edu.controller;

import com.edu.entity.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CommentController implements ControllerSpecification {
    private static final String PATH = "/comments";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.ALL)
                .build();
    }

    public String getJsonBody(Comment entity) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(entity);
            log.info("Generated JSON:\r\n" + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
