package com.samsung.sds.emarket.notification.service;

import com.samsung.sds.emarket.notification.model.CampaignDTO;
import org.springframework.messaging.Message;

import java.io.IOException;

public interface NotificationService {

    String postMessage(Message<CampaignDTO> message) throws IOException;
}
