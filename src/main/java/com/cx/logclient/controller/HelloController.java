package com.cx.logclient.controller;

import com.cx.logclient.kafka.KafkaSender;
import com.cx.logclient.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

@RestController

public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @Resource
    private HelloService helloService;

    @Autowired
    private KafkaSender kafkaSender;

    @RequestMapping("/hello")
    public String hello() {
        LOG.debug("controller debug......");
        LOG.info("controller info......");
        LOG.warn("controller warn......");
        LOG.error("controller error......");
//        for(int i=0;i<100;i++){
//            Random rand = new Random();
//            kafkaSender.sendChannelMess("test", rand.nextInt(100)+"");
//        }
        return "Hello World!";
    }


    @RequestMapping("/hello/service")
    public String helloService() {
        helloService.hello();
        return "Hello Service!";
    }

}
