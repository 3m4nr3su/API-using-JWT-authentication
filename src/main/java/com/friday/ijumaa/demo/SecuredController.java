package com.friday.ijumaa.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured")
public class SecuredController {
	
	@GetMapping("/endpoint")
	public ResponseEntity<String> securedEndpoint(@RequestHeader("Authorization") String jwtToken) {
		return ResponseEntity.ok("Sabalkheri, bitch!");

	}

}
