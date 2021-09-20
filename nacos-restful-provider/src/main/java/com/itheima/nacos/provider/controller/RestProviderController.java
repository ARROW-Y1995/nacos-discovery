package com.itheima.nacos.provider.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestProviderController {

    @GetMapping("/service")
    public String service() {
        return "create service to provider ";
    }
}
