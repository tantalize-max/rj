package com.cqupt.reggie.controller;

import com.cqupt.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/addreddBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
}