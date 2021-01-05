package com.qqy.stockWealth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WealthController {

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "Hello World";
    }
}
