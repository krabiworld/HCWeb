package org.headcrab.web.service;

import org.headcrab.web.model.Post;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface PostService {

	Optional<Post> getById(int id);

	Collection<Post> getAll();

	void save(Post post);

	void delete(Post post);

}
