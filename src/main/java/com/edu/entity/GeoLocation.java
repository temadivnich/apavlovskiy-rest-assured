package com.edu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoLocation {
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("lng")
    private String longitude;

}
