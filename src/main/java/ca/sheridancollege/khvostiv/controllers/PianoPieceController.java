package ca.sheridancollege.khvostiv.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.services.PianoPieceService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/pieces")
@AllArgsConstructor
public class PianoPieceController {

	private PianoPieceService pianoPieceService;

	@GetMapping
	public List<PianoPiece> getPieceCollection(Authentication authentication) {

		return pianoPieceService.findAllByUser(authentication.getName());
	}

	@GetMapping("/{id}")
	public PianoPiece getIndividualPiece(@PathVariable Long id, Authentication authentication) {

		return pianoPieceService.findByIdAndUser(id, authentication.getName());
	}

	@PostMapping(consumes = "application/json")
	public PianoPiece postPiece(@RequestBody PianoPiece pianoPiece, Authentication authentication) {

		return pianoPieceService.save(pianoPiece, authentication.getName());
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public PianoPiece putIndividualPiece(@RequestBody PianoPiece newPiece, @PathVariable Long id,
			Authentication authentication) {

		return pianoPieceService.update(id, newPiece, authentication.getName());
	}

	@DeleteMapping("/{id}")
	public String deleteIndividualPiece(@PathVariable Long id, Authentication authentication) {

		pianoPieceService.delete(id, authentication.getName());

		return "Piano piece with ID " + id + " was deleted.";
	}
}