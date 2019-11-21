package com.edu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {

       @JsonProperty("id")
       private Long id;
       @JsonProperty("name")
       private String name;
       @JsonProperty("username")
       private String username;
       @JsonProperty("email")
       private String email;
       @JsonProperty("phone")
       private String phone;
       @JsonProperty("website")
       private String website;
       @JsonProperty("address")
       private Address address;
       @JsonProperty("company")
       private Company company;
}
