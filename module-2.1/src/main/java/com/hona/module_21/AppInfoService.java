package com.hona.module_21;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class AppInfoService {

    private final AppProperties appProperties;
    private final String appVersion;
    private final String greeting;

    public AppInfoService(
            AppProperties appProperties,
            @Qualifier("appVersion") String appVersion,
            @Qualifier("specialGreeting") String greeting) {
        this.appProperties = appProperties;
        this.appVersion = appVersion;
        this.greeting = greeting;
    }

    public String getAppInfo() {
        return greeting + " | "
                + appProperties.getName() + " | "
                + appProperties.getMessage() + " | "
                + appVersion;
    }

    public String getRequestIds() {
        return requestIdPlaceholder();
    }

    private String requestIdPlaceholder() {
        return "Keep your existing request-ID implementation here";
    }
}