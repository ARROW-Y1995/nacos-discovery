package com.itheima.nacos.consumer.controller;

import com.itheima.microservice.service2.api.Service1Api;
import com.itheima.microservice.service2.service.Service2Api;
import com.itheima.nacos.consumer.utils.HttpClientUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;

@RestController
public class RestfulConsumerController {

    //服务id
    private String serverId="nacos-restful-provider";

    //注入配置文件中的提供者地址
    @Value("${providerAddress}")
    private String providerAddress;

    @Resource
    private LoadBalancerClient loadBalancerClient;

    @Reference
    private Service1Api service1Api;
    @Reference
    private Service2Api service2Api;

    @Value("${common.name}")
    private String name;

    @Resource
    private ApplicationContext applicationContext; //注入spring容器

    @GetMapping(value = "get/service")
    public String getService() {
        //新建restTemplate对象 此对象类似于
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://"+providerAddress+"/service", String.class);
        //使用HttpClientUtil工具类来实现restful调用
        String result1 = HttpClientUtil.httpGetRequest("http://" + providerAddress + "/service");
        //使用loadBalancerClient来实现客户端负载均衡调用接口
        ServiceInstance serviceInstance = loadBalancerClient.choose(serverId);
        URI uri =serviceInstance.getUri();
        System.out.println("uri:"+uri);
        String  result3 = restTemplate.getForObject(uri + "/service", String.class);

        String s = service1Api.service1api();

        String property = applicationContext.getEnvironment().getProperty("common.name");
        String addr = applicationContext.getEnvironment().getProperty("common.addr");

        return "get service success ;result: "+result+"| result1: "+result1 +"| result3: "+result3+" |s: "+s+" |name:"+name+" | property : "+property+" | addr: "+addr;
    }
}
