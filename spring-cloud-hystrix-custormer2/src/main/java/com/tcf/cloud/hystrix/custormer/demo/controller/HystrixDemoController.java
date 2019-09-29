package com.tcf.cloud.hystrix.custormer.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/***
 * TODO TCF 服务熔断控制器
 * @author 71485
 *
 */
@RestController
@RequestMapping("/")
public class HystrixDemoController {

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/test.do")
	@HystrixCommand(fallbackMethod="fallBack",threadPoolKey="userNamePooled")
	public String test()
	{
		String userName="李四";
		
		Map<String,Object> dataMap=new HashMap<String,Object>();
		dataMap.put("userName",userName);
		
		ResponseEntity<String> result=restTemplate.exchange(
				"http://feign-provider/selectByUserName/{userName}", 
				HttpMethod.POST,
				null,
				String.class,userName);
		
		return result.getBody();
	}
	
	//TODO TCF 降级处理方法
	public String fallBack()
	{
		return "error";
	}
	
}
