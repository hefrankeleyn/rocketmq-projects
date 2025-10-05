package com.demo.mq.demo01.conf;


import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.shaded.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @Author: lifei
 * @Date: 2025/10/2
 * @Description: 消费者
 **/
@Configuration
public class RocketMQConsumerConf {

    @Autowired
    private ClientServiceProvider provider;

    @Value("${rocketmq.topic}")
    private String topic;

    @Value("${rocketmq.proxy-endpoint}")
    private String endpoint;

    @Bean
    public PushConsumer getSpringRunner() {
        String consumerGroup = "test-01";
        String tag = "demo";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoint)
                .build();
        try {
             return provider.newPushConsumerBuilder()
                    .setClientConfiguration(clientConfiguration)
                    .setConsumerGroup(consumerGroup)
                    .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                    .setMessageListener(messageView -> {
                        String msg = StandardCharsets.UTF_8.decode(messageView.getBody().asReadOnlyBuffer()).toString();
                        System.out.println(Strings.lenientFormat("==>消费到消息：%s, view: %s", msg, messageView));
                        return ConsumeResult.SUCCESS;
                    }).build();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
