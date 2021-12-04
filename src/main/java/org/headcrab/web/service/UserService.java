package org.headcrab.web.service;


import org.headcrab.web.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findById(int id);

	void save(User user);

}
