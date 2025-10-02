package com.demo.mq.demo01.conf;


import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lifei
 * @Date: 2025/10/2
 * @Description: 装配RocketMQ的bean
 **/
@Configuration
public class RocketMQProducterConf {

    @Value("${rocketmq.proxy-endpoint}")
    private String endpoint;

    @Value("${rocketmq.topic}")
    private String topic;

    @Bean
    public ClientServiceProvider provider() {
        return ClientServiceProvider.loadService();
    }

    @Bean
    public Producer mqProducer(ClientServiceProvider clientServiceProvider) throws ClientException {
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoint).build();
        return clientServiceProvider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(clientConfiguration)
                .build();
    }


}
