package com.liqian.apiclient.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author lee
 * @date 2021/2/28 4:20 下午
 */
@FeignClient("apiservice")
public interface ConfigInterface {

    @GetMapping("/config")
    String getConfig();
}
