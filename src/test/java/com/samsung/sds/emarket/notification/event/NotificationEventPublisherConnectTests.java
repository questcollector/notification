package com.samsung.sds.emarket.notification.event;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;

@SpringBootTest
public class NotificationEventPublisherConnectTests {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private NotificationEventPublisher notificationEventPublisher;

    @Test
    public void test_publish_notificationSuccessEvent() {
        String testMessage = "{\"text\":\"test message\"}";

        notificationEventPublisher.publishNotificationSuccessEvent(testMessage);

        byte[] received = outputDestination.receive(100, "notificationSuccessEvent").getPayload();

        String payload = new String(received);

        Assertions.assertThat(payload).isEqualTo(testMessage);

    }

    @Test
    public void test_publish_notificationFailedEvent() {
        String testMessage = "test error message";

        notificationEventPublisher.publishNotificationFailedEvent(testMessage);

        byte[] received = outputDestination.receive(100, "notificationFailedEvent").getPayload();

        String payload = new String(received);

        Assertions.assertThat(payload).isEqualTo(testMessage);
    }
}
