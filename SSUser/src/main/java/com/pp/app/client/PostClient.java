package com.pp.app.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="post")
public interface PostClient {

	@GetMapping("/{id}")
	public String getUserPosts(@PathVariable("id")int id);
	
	@GetMapping("/port")
	public String retrievePort();
}
