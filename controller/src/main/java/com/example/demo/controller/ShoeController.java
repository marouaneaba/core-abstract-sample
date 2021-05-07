package com.example.demo.controller;

import com.example.demo.dto.shoe.in.ShoeFilter;
import com.example.demo.dto.shoe.out.Shoes;
import com.example.demo.facade.ShoeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.ShopApiEndpoints;

@Controller
@RequestMapping(path = ShopApiEndpoints.SHOES)
@RequiredArgsConstructor
public class ShoeController {

  private final ShoeFacade shoeFacade;

  @GetMapping(path = ShopApiEndpoints.SEARCH)
  public ResponseEntity<Shoes> all(ShoeFilter filter, @RequestHeader Integer version) {
    return ResponseEntity.ok(shoeFacade.get(version).search(filter));
  }
}
