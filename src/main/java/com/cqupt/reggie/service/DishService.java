package com.cqupt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.reggie.dto.DishDto;
import com.cqupt.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);


    DishDto getByIdWithFlavor(Long id);

    void updateFlavor(DishDto dishDto);

    void modifyStatusById(Integer status, List<Long> ids);
}
