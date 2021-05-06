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

  @NotEmpty(message = "Name is required")
  @JsonIgnore
  private String name;

  @NotNull(message = "Size is required")
  private Integer size;

  @NotBlank(message = "Color is required")
  private Color color;

  @Positive(message = "Quantity cannot be negative")
  @Max(message = "Quantity is less than thirty", value = 30)
  @NotNull(message = "Quantity is required")
  private Integer quantity;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockItemBuilder {}
}
