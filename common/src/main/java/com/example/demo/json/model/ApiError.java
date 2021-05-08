package com.example.demo.json.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private List<?> details;
}
