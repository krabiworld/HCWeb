package org.headcrab.web.repository;

import org.headcrab.web.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	Collection<Post> findAllByOrderByDate();

	Optional<Post> findById(int id);

}
