package com.cx.logclient.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.cx.logclient.kafka.KafkaSender;
import com.cx.logclient.vo.LogMessage;
import com.cx.logclient.vo.LogMessageBuilder;

/**
 * @author: cx
 * @Description:
 * @Date: Created in 10:30 2018/9/14
*/
public class KafkaAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private String topic="application.log";
    @Override
    protected void append(ILoggingEvent event) {
        if (event != null && event.getMessage() != null) {
           LogMessage message= LogMessageBuilder.build(event);
            message.setAppId("00000");
            message.setMessage(event.getMessage());
            KafkaSender.sendMsgToKafka(message,topic);
        }
    }
}
