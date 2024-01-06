package com.samsung.sds.emarket.notification.service.slack;

import com.samsung.sds.emarket.notification.event.NotificationEventPublisher;
import com.samsung.sds.emarket.notification.model.CampaignDTO;
import com.samsung.sds.emarket.notification.service.NotificationService;
import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Value("${slack.webhook.url}")
    private String WEBHOOK_URL;
    private final Slack slack;

    private final NotificationEventPublisher notificationEventPublisher;

    public NotificationServiceImpl(Slack slack, NotificationEventPublisher notificationEventPublisher) {
        this.slack = slack;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    @Override
    public String postMessage(Message<CampaignDTO> message) throws IOException {

        String content = buildMessageContent(message);

        WebhookResponse response = slack.send(WEBHOOK_URL, content);

        int responseStatusCode = response.getCode();

        if (responseStatusCode == 200) {
            notificationEventPublisher.publishNotificationSuccessEvent(content);
        } else {
            throw new IOException(response.toString());
        }

        return content;
    }

    /**
     *
     * Generate Slack Message payload with Message
     * <br>
     * Slack Message formatting mkdwn reference:
     * <br>
     * https://api.slack.com/reference/surfaces/formatting
     *
     * @param message Message&lt;CampaignDTO&gt; contains Headers (SlackUserId,IpAddress, Hostname), Payload(CampaignDTO)
     * @return Formatted String as Slack postMessage api palyload
     *
     */
    private String buildMessageContent(Message<CampaignDTO> message) {
        CampaignDTO campaignDTO = message.getPayload();
        MessageHeaders headers = message.getHeaders();

        StringBuilder sb = new StringBuilder();
        sb.append("{\"text\":\"");
        sb.append("(Ads) [eMarket] *" + campaignDTO.getCampaignName() + "*\n\n");
        sb.append("Hi <@").append(headers.get("SlackUserId")).append(">, ");
        sb.append("We are giving you new promotion from eMarket.").append("\n");
        sb.append(">- :point_right: :star:").append(campaignDTO.getDescription()).append(":star:").append("\n");
        sb.append(">- from <!date^").append(campaignDTO.getStartDate().toEpochSecond()).append("^{date_num} {time}|2022-05-18 05:01 AM> ");
        sb.append("until <!date^").append(campaignDTO.getEndDate().toEpochSecond()).append("^{date_num} {time}|2022-06-17 05:01 AM>").append("\n");
        sb.append(">- banner: ").append(campaignDTO.getPictureUri()).append("\n");
        sb.append(">- details: ").append(campaignDTO.getDetailsUri()).append("\n");
        sb.append("\"}");
        return sb.toString();
    }
}
