package com.example.demo.dto.out.stock;

import com.example.demo.dto.in.ShoeFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
@JsonDeserialize(builder = StockOutline.StockOutlineBuilder.class)
public class StockOutline implements Serializable {

    @JsonProperty("color")
    private ShoeFilter.Color color;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonPOJOBuilder(withPrefix = "")
    public static class StockOutlineBuilder {

    }
}
