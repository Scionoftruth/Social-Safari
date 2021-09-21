package com.pp.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pp.app.models.Post;

public interface PostRepo extends JpaRepository<Post,Integer>{
	
	public List<Post> findAll();
	public List<Post> findAllByUser(int u);
	public Post findById(int postId);

}
