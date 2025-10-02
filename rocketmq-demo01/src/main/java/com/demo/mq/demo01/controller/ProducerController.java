package com.demo.mq.demo01.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @Author: lifei
 * @Date: 2025/10/2
 * @Description: 生产者
 **/
@RestController
@RequestMapping(value = "/producer")
public class ProducerController {

    private static final Logger log = LogManager.getLogger(ProducerController.class);

    @Value("${rocketmq.topic}")
    private String topic;
    @Autowired
    private Producer mqProducer;
    @Autowired
    private ClientServiceProvider provider;

    @GetMapping(value = "/send")
    public String sendMessage(@RequestParam(value = "msg") String msg) {
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                .setKeys("1")
                .setTag("demo")
                .setBody(msg.getBytes(StandardCharsets.UTF_8))
                .build();
        try {
            SendReceipt sendReceipt = mqProducer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
            return "success";
        }catch (Exception e) {
            log.error("send error: {}", e.getMessage(), e);
            return "error";
        }
    }

    @GetMapping(value = "/say")
    public String sayHello() {
        return "Hello RocketMQ!";
    }
}
