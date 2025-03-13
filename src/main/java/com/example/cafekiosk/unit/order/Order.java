package com.example.cafekiosk.unit.order;

import com.example.cafekiosk.unit.beverage.Beverage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime localDateTime;
    private final List<Beverage> beverages;

}
