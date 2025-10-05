package com.demo.mq.demo01.controller;


import com.demo.mq.demo01.beans.MsgDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 异步发送消息
     * @param dto
     * @Return java.lang.String
     * @Date 2025/10/5
     */
    @PostMapping(value = "/send/async")
    public String asyncSendMessage(@RequestBody MsgDTO dto) {
        if (StringUtil.isNullOrEmpty(dto.getMsg())) {
            return "error";
        }
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                .setKeys("1")
                .setTag("demo")
                .setBody(dto.getMsg().getBytes(StandardCharsets.UTF_8))
                .build();
        CompletableFuture<SendReceipt> future = mqProducer.sendAsync(message);
        future.whenComplete((r, e)->{
            if (Objects.nonNull(e)) {
                return;
            }
            System.out.println("Async send message success");
        });
        return "success";
    }

    @GetMapping(value = "/say")
    public String sayHello() {
        return "Hello RocketMQ!";
    }
}
