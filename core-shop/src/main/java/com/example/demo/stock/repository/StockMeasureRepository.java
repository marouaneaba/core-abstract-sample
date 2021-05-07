package com.example.demo.stock.repository;

import com.example.demo.dto.shoe.Color;
import com.example.demo.stock.entity.StockMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockMeasureRepository extends JpaRepository<StockMeasure, Long> {
    Optional<StockMeasure> findByShoe_ColorAndShoe_NameAndShoe_Size(Color color, String name, Integer size);
}
