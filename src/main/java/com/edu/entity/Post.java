package com.edu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Post {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String body;

}
