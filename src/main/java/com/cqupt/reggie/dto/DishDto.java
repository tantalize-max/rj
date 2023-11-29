package com.cqupt.reggie.dto;

import com.cqupt.reggie.entity.Dish;
import com.cqupt.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    //对应的前端
    private String categoryName;

    private Integer copies;
}
