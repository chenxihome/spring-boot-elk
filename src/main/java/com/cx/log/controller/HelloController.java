package com.cx.log.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String hello() {
        LOG.debug("controller debug......");
        LOG.info("controller info......");
        LOG.warn("controller warn......");
        LOG.error("controller error......");
        return "Hello World!";
    }
}
