package com.cqupt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.reggie.dto.SetmealDto;
import com.cqupt.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
