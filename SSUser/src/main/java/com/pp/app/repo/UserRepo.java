package com.pp.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pp.app.models.User;

public interface UserRepo extends JpaRepository<User,Integer>{

	public List<User> findAll();
	public User findByUsername(String username);
	public User findById(int id);
}
