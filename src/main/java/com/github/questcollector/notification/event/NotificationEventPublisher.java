package com.github.questcollector.notification.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventPublisher {
    public static final String NOTIFICATION_SUCCESS_EVENT = "notificationSuccessEvent-out-0";
    public static final String NOTIFICATION_FAILED_EVENT = "notificationFailedEvent-out-0";
    private final StreamBridge streamBridge;

    public NotificationEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishNotificationSuccessEvent(String content) {
        streamBridge.send(NOTIFICATION_SUCCESS_EVENT, content);
        log.info("published message to " + NOTIFICATION_SUCCESS_EVENT + " channel \n"
                + "message content: " + content);
    }

    public void publishNotificationFailedEvent(String errorMessage) {
        streamBridge.send(NOTIFICATION_FAILED_EVENT, errorMessage);
        log.info("published message to " + NOTIFICATION_FAILED_EVENT + " channel \n"
                + "message content: " + errorMessage);
    }
}