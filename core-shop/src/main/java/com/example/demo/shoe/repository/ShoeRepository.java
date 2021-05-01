package com.example.demo.shoe.repository;

import com.example.demo.shoe.entity.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Long> {

}
