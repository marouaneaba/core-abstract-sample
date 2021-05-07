package com.example.demo.dto.stock.out;

import com.example.demo.dto.shoe.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.io.Serializable;

@Value
@Builder
@JsonDeserialize(builder = StockItem.StockItemBuilder.class)
public class StockItem implements Serializable {

  @JsonIgnore private Long id;

  @NotBlank(message = "Name is not blank")
  @JsonIgnore
  private String name;

  @NotNull(message = "Size is required")
  @PositiveOrZero(message = "Size is positive or zero")
  private Integer size;

  @NotNull(message = "Color is required")
  private Color color;

  @Max(message = "Quantity is between  [ 0 , 30 ]", value = 30)
  @Min(message = "Quantity is between  [ 0 , 30 ]", value = 0L)
  @NotNull(message = "Quantity is required")
  private Integer quantity;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockItemBuilder {}
}
