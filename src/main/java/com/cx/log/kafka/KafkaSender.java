package com.cx.log.kafka;

import com.alibaba.fastjson.JSON;
import com.cx.log.common.vo.LogMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 11:44 2018/9/14
 */
@Component
public class KafkaSender {
    private static Producer<String, String> producer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.113.31.159:9092");//服务器ip:端口号，集群用逗号分隔
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    public static void sendMsgToKafka(LogMessage msg, String topic) {
        producer.send(new ProducerRecord<String, String>(topic, String.valueOf(System.currentTimeMillis()),
                JSON.toJSONString(msg)));
        System.out.println("kafka发送消息是：" + JSON.toJSONString(msg));
    }

    public static void closeKafkaProducer() {
        producer.close();
    }

}
