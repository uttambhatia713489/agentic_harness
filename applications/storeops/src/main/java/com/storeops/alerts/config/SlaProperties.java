package com.storeops.alerts.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storeops.sla")
public class SlaProperties {

    private Duration gracePeriod = Duration.ofHours(4);

    public Duration getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(final Duration gracePeriod) {
        this.gracePeriod = gracePeriod;
    }
}
