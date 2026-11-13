package ca.sheridancollege.khvostiv.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.repositories.PianoPieceRepository;

@SpringBootTest
public class PianoPieceRepositoryIntegrationTests {

	@Autowired
	private PianoPieceRepository pianoPieceRepository;

	@Test
	public void testPianoPieceCrudOperations() {

		// CREATE
		PianoPiece piece = new PianoPiece();
		piece.setTitle("Test Piano Piece");
		piece.setDifficulty("Beginner");

		PianoPiece savedPiece = pianoPieceRepository.save(piece);

		assertNotNull(savedPiece.getId());

		// RETRIEVE
		Optional<PianoPiece> retrievedPiece = pianoPieceRepository.findById(savedPiece.getId());

		assertTrue(retrievedPiece.isPresent());

		assertEquals("Test Piano Piece", retrievedPiece.get().getTitle());

		// UPDATE
		savedPiece.setTitle("Updated Test Piano Piece");
		pianoPieceRepository.save(savedPiece);

		PianoPiece updatedPiece = pianoPieceRepository.findById(savedPiece.getId()).get();

		assertEquals("Updated Test Piano Piece", updatedPiece.getTitle());

		// DELETE
		pianoPieceRepository.deleteById(savedPiece.getId());

		Optional<PianoPiece> deletedPiece = pianoPieceRepository.findById(savedPiece.getId());

		assertFalse(deletedPiece.isPresent());
	}
}