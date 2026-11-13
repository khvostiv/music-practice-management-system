package ca.sheridancollege.khvostiv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.khvostiv.models.AuthenticationRequest;
import ca.sheridancollege.khvostiv.models.AuthenticationResponse;
import ca.sheridancollege.khvostiv.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request,
			HttpServletResponse response) {

		AuthenticationResponse authenticationResponse = authenticationService.register(request);

		addJwtCookie(response, authenticationResponse.getToken());

		return ResponseEntity.ok(authenticationResponse);
	}

	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
			HttpServletResponse response) {

		AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

		addJwtCookie(response, authenticationResponse.getToken());

		return ResponseEntity.ok(authenticationResponse);
	}

	private void addJwtCookie(HttpServletResponse response, String token) {

		Cookie cookie = new Cookie("jwt", token);

		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(24 * 60 * 60);

		response.addCookie(cookie);
	}
}