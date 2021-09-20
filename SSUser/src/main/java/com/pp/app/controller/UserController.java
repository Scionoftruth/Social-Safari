package com.pp.app.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pp.app.client.PostClient;
import com.pp.app.models.User;
import com.pp.app.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor()
@NoArgsConstructor
@CrossOrigin(origins="*")
public class UserController {

	@Autowired
	private UserService uServ;
	
	@Autowired
	private PostClient pClient;
	
	@Autowired
	private CircuitBreakerFactory<?,?> cbFactory;
	
	private ResponseEntity<String> retrievePortFallback(){
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("PostService is currently unavailable. Please check back later");
	}
	
	@PostMapping("/port")
	public ResponseEntity<String> retrievePort(){
		return cbFactory.create("post-port").run(() -> new ResponseEntity<String>(pClient.retrievePort(), HttpStatus.OK),
				throwable -> retrievePortFallback());
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> createUser(@RequestBody LinkedHashMap<String, String> user){
		User u = new User(user.get("firstName"),user.get("lastname"),user.get("username"),user.get("email"),user.get("password"));
		if(uServ.registerUser(u)) {
			return new ResponseEntity<String>("User was created", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String>("Username or email already taken", HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestBody LinkedHashMap<String, String> user){
		User u = uServ.loginUser(user.get("username"), user.get("password"));
		if(u==null) {
			return new ResponseEntity<User>(u,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(HttpStatus.OK);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username){
		User u = uServ.displayUser(username);
		if(u==null) {
			return new ResponseEntity<User>(u,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(u,HttpStatus.OK);
	}
	
	private ResponseEntity<String> getUserPostsFallback(){
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("PostService is currently unavailable. Please check back later");
	}
	
	@GetMapping("/posts/{id}")
	public ResponseEntity<String> getUserPosts(@PathVariable("id")int id){
		return cbFactory.create("get-posts").run(() -> new ResponseEntity<String>(pClient.getUserPosts(id), HttpStatus.OK),
				throwable -> getUserPostsFallback());
	}
	
}
