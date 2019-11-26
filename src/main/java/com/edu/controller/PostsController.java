package com.edu.controller;

import com.edu.entity.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostsController implements ControllerSpecification {
    private static final String PATH = "/posts/{postId}";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
//                .addHeaders(getBaseHeader())
                .log(LogDetail.ALL)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.ALL)
                .build();
    }

    public String getJsonBody(Post postEntity) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(postEntity);
            log.info("Generated JSON:\r\n" + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
