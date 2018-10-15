package com.cx.logclient.common.config;

public interface KafkaProperties
{
    final static String zkConnect = "10.113.31.159:2181";
    final static String groupId = "test-consumer-group";
    final static String topic = "application.log";
    final static String kafkaServerURL = "10.113.31.159:9092";
    final static int kafkaServerPort = 9092;
    final static int kafkaProducerBufferSize = 64 * 1024;
    final static int connectionTimeOut = 20000;
    final static int reconnectInterval = 10000;
    final static String topic2 = "topic2";
    final static String topic3 = "topic3";
    final static String clientId = "SimpleConsumerDemoClient";
}
