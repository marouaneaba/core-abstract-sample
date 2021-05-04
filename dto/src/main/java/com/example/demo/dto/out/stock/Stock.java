package com.example.demo.dto.out.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode
@JsonDeserialize(builder = Stock.StockBuilder.class)
public class Stock implements Serializable {

    @JsonProperty("state")
    private State state;

    @JsonProperty("shoes")
    private List<StockOutline> shoes;

    @JsonPOJOBuilder(withPrefix = "")
    public static class StockBuilder {

    }

    public enum State{

        EMPTY,
        FULL,
        SOME,
        ;

    }
}
