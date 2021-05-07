package com.example.demo.stock.entity;

import com.example.demo.shoe.entity.Shoe;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "stock_outline")
public class StockMeasure implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @EqualsAndHashCode.Include()
    @NotNull(message = "Constraint shoe is not null")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_shoe", referencedColumnName = "id")
    private Shoe shoe;

    @Max(message = "Quantity is between  [ 0 , 30 ]", value = 30)
    @Min(message = "Quantity is between  [ 0 , 30 ]", value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public void addQuantity(Integer quantity){
        this.quantity = this.quantity + quantity;
    }

}
