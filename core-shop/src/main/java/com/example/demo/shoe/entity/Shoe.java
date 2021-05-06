package com.example.demo.shoe.entity;

import com.example.demo.dto.shoe.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "shoe")
public class Shoe implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @Column(name = "color", nullable = false)
  private Color color;

  @Column(name = "size", nullable = false)
  private Integer size;
}
