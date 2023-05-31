package com.friday.ijumaa.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.friday.ijumaa.config.JwtService;
import com.friday.ijumaa.refreshjwt.RefreshTokenService;
import com.friday.ijumaa.user.Role;
import com.friday.ijumaa.user.User;
import com.friday.ijumaa.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;


	public AuthenticationResponse register(RegisterRequest request) {
		var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
				.build();
		repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = refreshTokenService.createRefreshToken(request.getEmail());
		
		return AuthenticationResponse.builder().token(jwtToken).refreshtoken(refreshToken.getToken()).build();
	}

	public AuthenticationResponse authenticate(AuthRequestTemp request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

				);
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = refreshTokenService.findByToken(request.getRefreshtoken());
		return AuthenticationResponse.builder().token(jwtToken).refreshtoken(refreshToken.get().getToken()).build();
	}



}
