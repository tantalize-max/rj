package com.cqupt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.reggie.entity.Orders;


public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
