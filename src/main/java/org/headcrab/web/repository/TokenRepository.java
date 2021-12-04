package org.headcrab.web.repository;

import org.headcrab.web.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

	Optional<Token> findByIdAndTokenAndType(int id, String token, String type);

}
