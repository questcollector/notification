package com.github.questcollector.notification.event;

import com.github.questcollector.notification.model.CampaignDTO;
import com.github.questcollector.notification.service.NotificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CampaignEventListenerConnectTests {

    @SpyBean
    private CampaignAddedEventHandler campaignAddedEventHandler;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private InputDestination inputDestination;

    @Captor
    private ArgumentCaptor<Message<CampaignDTO>> captor;

    @Test
    void test_listen_campaignAddedEvent() throws IOException {
        // Set Message Payload
        String name = "test name";
        OffsetDateTime from = OffsetDateTime.parse("2022-05-18T05:01:43+09:00");
        OffsetDateTime to = OffsetDateTime.parse("2022-06-17T05:01:43+09:00");

        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignId(100);
        campaignDTO.setCampaignName(name);
        campaignDTO.setDescription("desc");
        campaignDTO.setPictureUri("pictureUri");
        campaignDTO.setDetailsUri("detailUri");
        campaignDTO.setStartDate(from);
        campaignDTO.setEndDate(to);

        // send campaignAddedEvent
        inputDestination.send(new GenericMessage<>(campaignDTO), "campaignAddedEvent");

        // verify listener invoked
        verify(campaignAddedEventHandler, times(1)).campaignAddedEvent();
        // capture message
        verify(notificationService, times(1)).postMessage(captor.capture());

        CampaignDTO payload = captor.getValue().getPayload();

        Assertions.assertThat(payload)
                .hasFieldOrPropertyWithValue("campaignId", 100)
                .hasFieldOrPropertyWithValue("campaignName", name);
    }
}
