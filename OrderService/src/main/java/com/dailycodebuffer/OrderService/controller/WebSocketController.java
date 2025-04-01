package com.dailycodebuffer.OrderService.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/send-order")  // Listens for messages from frontend
    public void processOrderMessage(String message) {
        // Process the message without broadcasting
        System.out.println("Received Order Message: " + message);
    }
}
