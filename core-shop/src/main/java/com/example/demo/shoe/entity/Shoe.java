package com.example.demo.shoe.entity;

import com.example.demo.dto.in.ShoeFilter;
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
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private ShoeFilter.Color color;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
