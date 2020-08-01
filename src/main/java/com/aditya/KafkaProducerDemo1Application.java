package com.aditya;

import com.aditya.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class KafkaProducerDemo1Application {

    @Value("${topic-name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerDemo1Application.class, args);
    }

    @PostConstruct
    public void sendMessage() throws JsonProcessingException {

        Account account = new Account();
        account.setAccountId(1234);
        account.setCurrency("INR");
        account.setBalance(1000);
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(account);
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topicName, str);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message :" + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                System.out.println("message sent successfully with offset" + stringObjectSendResult.getRecordMetadata().offset());
            }
        });
    }

}
