package com.friday.ijumaa.refreshjwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String token);

}
