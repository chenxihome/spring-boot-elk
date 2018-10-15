//package com.cx.logclient.kafka;
//
//import kafka.javaapi.consumer.ConsumerConnector;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.*;
////
///**
// * @author: cx
// * @Description:
// * @Date: Created in 15:49 2018/10/10
// */
//public class ConsumerServer {
//        protected static final Logger logger = LoggerFactory.getLogger(ConsumerServer.class);
//        private final ConsumerConnector consumer;
//        private final String signalTopic = "SIGNAL_ORDERINFO";
//        private final String followTopic = "FOLLOW_ORDERINFO";
//        private final String signalHisTopic = "HIS_ORDERINFO";
//        private final String followHisTopic = "FOLLOW_HIS_ORDERINFO";
//        private ConsumerConfig consumerConfig;
//        private static int threadNum = 6;
//        /**
//         * Set the ThreadPoolExecutor's core pool size.
//         */
//        private int corePoolSize = 6;
//        /**
//         * Set the ThreadPoolExecutor's maximum pool size.
//         */
//        private int maxPoolSize = 200;
//        /**
//         * Set the capacity for the ThreadPoolExecutor's BlockingQueue.
//         */
//        private int queueCapacity = 1024;
//        /**
//         * thread prefix name
//         */
//        private String ThreadNamePrefix = "kafka-consumer-pool-%d";
//
//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNameFormat(ThreadNamePrefix).build();
//
//        /**
//         * Common Thread Pool
//         */
//        ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(queueCapacity), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//
//        public ConsumerServer() {
//            Properties properties = new Properties();
//            //开发环境:10.0.2.22:2181
//            properties.put("zookeeper.connect", "10.0.2.22:2181");
//            // 组名称
//            properties.put("group.id", "back_consumer_group");
//            properties.put("key.deserializer", StringDeserializer.class);
//            properties.put("value.deserializer", StringDeserializer.class);
//            consumerConfig = new ConsumerConfig(properties);
//            consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
//        }
//
//        @Override
//        public void shutdown() {
//            if (consumer != null) {
//                consumer.shutdown();
//            }
//            if (pool != null) {
//                pool.shutdown();
//            }
//            try {
//                if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)) {
//                    System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
//                }
//            } catch (InterruptedException e) {
//                System.out.println("Interrupted during shutdown, exiting uncleanly");
//            }
//        }
//
//        public void run(int numThreads) {
//            Map<String, Integer> topicCountMap = Maps.newHashMap();
//            topicCountMap.put(signalTopic, new Integer(numThreads));
//            topicCountMap.put(followTopic, new Integer(numThreads));
//            topicCountMap.put(signalHisTopic, new Integer(numThreads));
//            topicCountMap.put(followHisTopic, new Integer(numThreads));
//            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
//            consumerMap.values().stream().forEach(value -> {
//                List<KafkaStream<byte[], byte[]>> streams = value;
//                int threadNumber = 0;
//                /**
//                 * 可以为每隔topic创建一个线程池，因为每个topic我设置的partition=6
//                 * （kafka consumer通过增加线程数来增加消费能力，但是需要足够的分区，如目前我设置的partition=6，那么并发可以启动6个线程同时消费）
//                 * ExecutorService pool = createThreadPool();
//                 */
//                for (final KafkaStream stream : streams) {
//                    pool.submit(new KafkaOrderConsumer(stream, threadNumber));
//                    threadNumber++;
//                }
//            });
//        }
//
//        /**
//         * 创建线程池
//         *
//         * @return
//         */
//        private ExecutorService createThreadPool() {
//            ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
//                    0L, TimeUnit.MILLISECONDS,
//                    new LinkedBlockingQueue<Runnable>(queueCapacity), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//            return pool;
//        }
//        public static void main(String[] args) {
//            int threads = 1;
//            if (args.length < 1) {
//                threads = threadNum;
//            } else {
//                threads = Integer.parseInt(args[0]);
//            }
//
//            ConsumerServer example = new ConsumerServer();
//            example.run(threads);
//            try {
//                Thread.sleep(Long.MAX_VALUE);
//            } catch (InterruptedException ie) {
//            }
//            example.shutdown();
//        }
//
//}
