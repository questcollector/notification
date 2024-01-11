package com.github.questcollector.notification.event;

import com.github.questcollector.notification.model.CampaignDTO;
import com.github.questcollector.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
@Slf4j
public class CampaignAddedEventHandler {
    private final NotificationService notificationService;
    private final NotificationEventPublisher notificationEventPublisher;

    public CampaignAddedEventHandler(NotificationService notificationService, NotificationEventPublisher notificationEventPublisher) {
        this.notificationService = notificationService;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    @Bean
    public Consumer<Message<CampaignDTO>> campaignAddedEvent() {
        return message -> {
            CampaignDTO campaignDTO = message.getPayload();

            log.info("received message from rabbitmq campaignAddedEvent.notification\n"
                    + "message payload: " + campaignDTO);

            try {
                notificationService.postMessage(message);
            } catch (IOException e) {
                notificationEventPublisher.publishNotificationFailedEvent(e.getMessage());
            }
        };
    }
}
