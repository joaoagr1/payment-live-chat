package com.payment_chat.project.service;

import com.payment_chat.project.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.payment_chat.project.config.RabbitMQConfig.CHAT_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageListener {

    @RabbitListener(queues = CHAT_QUEUE)
    public void handleChatMessage(MessageDTO message) {
        log.info("Received message from RabbitMQ: From {} to {}",
                message.getSenderUsername(),
                message.getRecipientUsername());

        // Here you can add additional processing like:
        // - Storing in cache
        // - Analytics
        // - External integrations
        // - Push notifications
    }
}
