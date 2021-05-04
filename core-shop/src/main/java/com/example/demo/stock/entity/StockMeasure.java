package com.example.demo.stock.entity;

import com.example.demo.shoe.entity.Shoe;
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
@Table(name = "stock_outline")
public class StockMeasure implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id",nullable = false)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_shoe", referencedColumnName = "id")
  private Shoe shoe;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  public void addQuantity(Integer quantity){
    this.quantity = this.quantity + quantity;
  }
}