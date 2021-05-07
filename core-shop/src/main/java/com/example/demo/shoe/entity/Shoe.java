package com.example.demo.shoe.entity;

import com.example.demo.dto.shoe.Color;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name = "shoe")
public class Shoe implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotBlank(message = "Name is mandatory")
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull(message = "Color is mandatory")
  @Enumerated(EnumType.STRING)
  @Column(name = "color", nullable = false)
  private Color color;

  @PositiveOrZero(message = "Size is positive or zero")
  @Column(name = "size", nullable = false)
  private Integer size;
}
