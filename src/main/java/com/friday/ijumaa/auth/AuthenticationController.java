package com.friday.ijumaa.auth;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friday.ijumaa.config.JwtService;
import com.friday.ijumaa.refreshjwt.RefreshToken;
import com.friday.ijumaa.refreshjwt.RefreshTokenRequest;
import com.friday.ijumaa.refreshjwt.RefreshTokenService;
import com.friday.ijumaa.user.Role;
import com.friday.ijumaa.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;
	private final RefreshTokenService refreshTokenService;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthRequestTemp request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh")
	public Optional<Object> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return refreshTokenService.findByToken(refreshTokenRequest.getRefreshtoken())
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUserInfo)
				.map(userInfo -> {
					
					var user = User.builder().firstname(userInfo.getFirstname()).lastname(userInfo.getLastname())
							.email(userInfo.getEmail()).password(passwordEncoder.encode(userInfo.getPassword()))
							.role(Role.USER).build();
					String accessToken = jwtService.generateToken(user);

					return AuthenticationResponse.builder()
							.refreshtoken(refreshTokenRequest.getRefreshtoken())
							.token(accessToken)
							.build();
				});
	}

}
