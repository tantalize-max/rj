package com.cqupt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.reggie.common.BaseContext;
import com.cqupt.reggie.common.CustomException;
import com.cqupt.reggie.common.R;
import com.cqupt.reggie.entity.AddressBook;
import com.cqupt.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService ;

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook={}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null , AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
        return R.success(addressBookList);
    }
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("currentId:{}",BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        //获取用户id
        addressBook.setUserId(BaseContext.getCurrentId());
        //构造条件构造器
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId ,addressBook.getUserId());
        //将所有全部设置为默认为 0
        updateWrapper.set(AddressBook::getIsDefault,0);
        // 执行更新操作
        addressBookService.update(updateWrapper);
        //将当前地址的is_default设置为 1
        addressBook.setIsDefault(1);
        //update
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    @GetMapping("/default")
    public R<AddressBook> defaultAddress(){
        //获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        //条件构造
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //当前用户
        queryWrapper.eq(userId != null ,AddressBook::getUserId, userId);
        //默认地址
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null){
            throw new CustomException("地址信息不存在");
        }
        return R.success(addressBook);
    }
    @PutMapping
    public R<String> updateAdd(@RequestBody AddressBook addressBook) {
        if (addressBook == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        addressBookService.updateById(addressBook);
        return R.success("地址修改成功");
    }
    @DeleteMapping()
    public R<String> deleteAdd(@RequestParam("ids") Long id) {
        if (id == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            throw new CustomException("地址信息不存在，请刷新重试");
        }
        addressBookService.removeById(id);
        return R.success("地址删除成功");
    }
}