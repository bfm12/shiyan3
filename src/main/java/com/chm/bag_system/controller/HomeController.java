package com.chm.bag_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 17:39
 **/
@Controller
public class HomeController {

    @GetMapping("/database")
    public String database(){
        return "database";
    }

    @GetMapping("/scatter")
    public String scatter(){
        return "scatter";
    }

    @GetMapping("/answer")
    public String answer(){
        return "answer";
    }

    @GetMapping("/order")
    public String order(){
        return "order";
    }
}
