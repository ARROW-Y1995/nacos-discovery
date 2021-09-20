package com.itheima.microservice.service1;

import com.itheima.microservice.service2.api.Service1Api;
import com.itheima.microservice.service2.service.Service2Api;
import org.apache.dubbo.config.annotation.Reference;


@org.apache.dubbo.config.annotation.Service
public class Service1ApiImpl implements Service1Api {

    @Reference
    private Service2Api service2Api;

    public String service1api() {
        String dubboService2Result = service2Api.dubboService2();
        return "service1api  |  "+dubboService2Result;
    }
}
