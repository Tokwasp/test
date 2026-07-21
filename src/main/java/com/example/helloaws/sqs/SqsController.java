package com.example.helloaws.sqs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqsController {

    private final SqsMessageSender sqsMessageSender;

    public SqsController(SqsMessageSender sqsMessageSender) {
        this.sqsMessageSender = sqsMessageSender;
    }

    @PostMapping("/send")
    public SendResponse send(@RequestBody SendRequest request) {
        String messageId = sqsMessageSender.send(request.message());
        return new SendResponse(messageId, request.message());
    }

    public record SendRequest(String message) {
    }

    public record SendResponse(String messageId, String message) {
    }
}
