package ca.sheridancollege.khvostiv.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.khvostiv.models.PracticeSessionRequest;
import ca.sheridancollege.khvostiv.models.PracticeSessionResponse;
import ca.sheridancollege.khvostiv.services.PracticeSessionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/practice-sessions")
@AllArgsConstructor
public class PracticeSessionController {

	private PracticeSessionService practiceSessionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PracticeSessionResponse save(@RequestBody PracticeSessionRequest request, Authentication authentication) {

		return practiceSessionService.save(request, authentication.getName());
	}

	@GetMapping
	public List<PracticeSessionResponse> findAll(Authentication authentication) {

		return practiceSessionService.findAllByUser(authentication.getName());
	}

	@GetMapping("/{id}")
	public PracticeSessionResponse findById(@PathVariable Long id, Authentication authentication) {

		return practiceSessionService.findByIdAndUser(id, authentication.getName());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, Authentication authentication) {

		practiceSessionService.delete(id, authentication.getName());
	}
}