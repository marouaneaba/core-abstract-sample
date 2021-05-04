package com.example.demo.dto.stock.out;

import com.example.demo.validation.StockCapacity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode
@JsonDeserialize(builder = Stock.StockBuilder.class)
public class Stock implements Serializable {


    @JsonProperty("state")
    @JsonIgnore
    private State state;

    @StockCapacity(message = "Stock capacity limited of 30 shoes.")
    @NotNull(message = "can not be null")
    @JsonProperty("shoes")
    private List<StockItem> shoes;

    @JsonPOJOBuilder(withPrefix = "")
    public static class StockBuilder {

    }

    public enum State{
        @JsonProperty("EMPTY")
        EMPTY,
        @JsonProperty("FULL")
        FULL,
        @JsonProperty("SOME")
        SOME,
        ;

    }
}
