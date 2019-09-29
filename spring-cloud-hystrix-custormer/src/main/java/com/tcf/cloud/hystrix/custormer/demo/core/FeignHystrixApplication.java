package com.tcf.cloud.hystrix.custormer.demo.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/****
 * TODO TCF 消费者服务熔断器Hystrix处理雪崩效应启动类
 * @author 71485
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages= {"com.tcf.cloud.hystrix.custormer.demo"})
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class FeignHystrixApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(FeignHystrixApplication.class,args);
	}
	
	//TODO TCF RestTemplate+Ribbon本地负载均衡调用服务
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
	
	//TODO TCF SpringBoot2.0版本Hystrix DashBoard仪表盘默认路径需要设置为/hystrix.stream
	//TODO TCF Hystrix DashBoard仪表盘默认监控单服务
	//TODO TCF 只监控服务中使用@HystrixCommand标注的加入服务熔断机制的方法，其他方法不会被Hystrix DashBoard仪表盘监控
	@Bean
    public ServletRegistrationBean getServlet() 
	{
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
	
}
