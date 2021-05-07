package com.example.demo.dto.shoe.out;

import com.example.demo.dto.shoe.out.Shoes.ShoesBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.demo.dto.shoe.out.Shoes.ShoesBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = ShoesBuilder.class)
public class Shoes {

  @JsonProperty("shoes")
  List<Shoe> shoes;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ShoesBuilder {

  }


}
