package org.headcrab.web.service;

import org.headcrab.web.model.Token;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TokenService {

	Optional<Token> findByIdAndTokenAndType(int id, String token, String type);

	void save(Token token);

	void delete(Token token);

}
