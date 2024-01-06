package com.samsung.sds.emarket.notification.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CampaignDTO {
    private int campaignId;
    private String campaignName;
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String pictureUri;
    private String detailsUri;

}
