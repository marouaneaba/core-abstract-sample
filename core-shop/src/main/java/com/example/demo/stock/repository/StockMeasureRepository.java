package com.example.demo.stock.repository;

import com.example.demo.stock.entity.StockMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMeasureRepository extends JpaRepository<StockMeasure, Long> {
}
