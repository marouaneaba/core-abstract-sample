package com.example.demo.core;

import com.example.demo.dto.shoe.in.ShoeFilter;
import com.example.demo.dto.shoe.out.Shoes;

public interface ShoeCore {

  Shoes search(ShoeFilter filter);

}
