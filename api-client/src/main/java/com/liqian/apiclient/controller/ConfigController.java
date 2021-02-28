package com.liqian.apiclient.controller;

import com.liqian.apiclient.dao.ConfigInterface;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lee
 * @date 2021/2/28 4:21 下午
 */
@RestController
@DefaultProperties(defaultFallback = "testHx")
public class ConfigController {

    @Autowired
    ConfigInterface configInterface;

    /**
     * 服务熔断
     * fallbackMethod                               服务降级方法
     * circuitBreaker.enabled                       是否开启断路器
     * circuitBreaker.requestVolumeThreshold        请求次数
     * circuitBreaker.sleepWindowInMilliseconds     时间窗口期
     * circuitBreaker.errorThresholdPercentage      失败率达到多少后跳闸
     * <p>
     * 以下配置意思是在10秒时间内请求10次，如果有6次是失败的，就触发熔断器
     * <p>
     * 注解@HystrixProperty中的属性在com.netflix.hystrix.HystrixCommandProperties类中查看
     */
    @HystrixCommand(
            fallbackMethod = "payment_Global_FailHandler",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",
                            value = "true"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",
                            value = "10"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",
                            value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",
                            value = "60"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1")
            }
    )
    @GetMapping("/test")
    public String test() {
        return configInterface.getConfig();
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",
                            value = "true"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",
                            value = "2"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",
                            value = "10000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",
                            value = "10"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "7")
            })
    @GetMapping("/t/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("===> id 不能为负数");
        }
        //hutool工具类的使用,等价于UUID.randomUUID().toString()
        return Thread.currentThread().getName() + " 调用成功，流水号： " + 1;
    }

    /**
     * 服务熔断触发的服务降级方法
     *
     * @param id
     * @return
     */
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能为负数，请稍后再试。id:" + id;
    }


    public String testHx() {
        System.out.println("--------进入请求");
        return "服务不可用。拒绝请求";
    }

    /**
     * 全局服务降级方法
     *
     * @return
     */
    public String payment_Global_FailHandler() {
        return "全局异常处理信息";
    }

}
