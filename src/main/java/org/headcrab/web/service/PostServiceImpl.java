package org.headcrab.web.service;

import org.headcrab.web.model.Post;
import org.headcrab.web.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	@Autowired
	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public Optional<Post> getById(int id) {
		return postRepository.findById(id);
	}

	@Override
	public Collection<Post> getAll() {
		return postRepository.findAllByOrderByDateDesc();
	}

	@Override
	public void save(Post post) {
		postRepository.saveAndFlush(post);
	}

	@Override
	public void delete(Post post) {
		postRepository.delete(post);
	}

}
