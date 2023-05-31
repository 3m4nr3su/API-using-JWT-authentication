package com.friday.ijumaa.refreshjwt;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friday.ijumaa.user.UserRepository;

@Service
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenRepo refreshTokenRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	public RefreshToken createRefreshToken(String username) {
		RefreshToken refreshToken = RefreshToken.builder()
		.userInfo(userRepository.findByEmail(username).get())
		.token(UUID.randomUUID().toString())
		.expiryDate(Instant.now().plusMillis(600000))
		.build();
		
		return refreshTokenRepo.save(refreshToken);
	}
	
	public Optional<RefreshToken> findByToken(String token){
		return refreshTokenRepo.findByToken(token);
	}
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now())<0) {
			refreshTokenRepo.delete(token);
			throw new RuntimeException(token.getToken() + " imeexpire jo");
		}
		return token; 
	}
	

}
