package org.headcrab.web.service;

import org.headcrab.web.model.Token;
import org.headcrab.web.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

	private final TokenRepository tokenRepository;

	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Optional<Token> findByIdAndTokenAndType(int id, String token, String type) {
		return tokenRepository.findByIdAndTokenAndType(id, token, type);
	}

	@Override
	public void save(Token token) {
		tokenRepository.saveAndFlush(token);
	}

	@Override
	public void delete(Token token) {
		tokenRepository.delete(token);
	}

}
