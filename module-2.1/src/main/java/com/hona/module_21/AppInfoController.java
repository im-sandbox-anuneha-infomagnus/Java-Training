package com.hona.module_21;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppInfoController {

    private final AppInfoService appInfoService;

    public AppInfoController(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @GetMapping("/app-info")
    public String getAppInfo() {
        return appInfoService.getAppInfo();
    }

    @GetMapping("/request-id")
    public String getRequestId() {
        return appInfoService.getRequestIds();
    }
}