package com.cx.log.service.impl;

import com.cx.log.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello() {
        LOG.debug("service debug......");
        LOG.info("service info......");
        LOG.warn("service warn......");
        LOG.error("service error......");
        System.out.printf(" --------------------- ------------ ------------- ");
        return "Hello Service!";
    }


}
