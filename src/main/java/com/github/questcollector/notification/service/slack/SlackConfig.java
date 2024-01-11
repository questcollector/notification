package com.github.questcollector.notification.service.slack;

import com.slack.api.Slack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Bean
    public Slack getSlackInstance() {
        return Slack.getInstance();
    }
}