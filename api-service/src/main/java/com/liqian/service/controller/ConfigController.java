package com.liqian.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee
 * @date 2021/2/27 2:56 下午
 */
@RefreshScope
@RestController
public class ConfigController {
    @Value("${project.name:sss}")
    private String projectName;

    @Value("${project.org:www}")
    private String projectOrg;

    @GetMapping("/config")
    public Map<String, Object> getConfig() throws InterruptedException {
        Thread.sleep(100000000);
        Map<String, Object> configMap = new HashMap();
        configMap.put("projectName", projectName);
        configMap.put("projectOrg", projectOrg);
        return configMap;
    }
}
