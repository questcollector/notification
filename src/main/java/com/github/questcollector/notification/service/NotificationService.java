package com.github.questcollector.notification.service;

import com.github.questcollector.notification.model.CampaignDTO;
import org.springframework.messaging.Message;

import java.io.IOException;

public interface NotificationService {

    String postMessage(Message<CampaignDTO> message) throws IOException;
}
