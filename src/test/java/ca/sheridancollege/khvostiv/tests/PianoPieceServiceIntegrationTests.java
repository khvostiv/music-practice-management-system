package ca.sheridancollege.khvostiv.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.beans.Role;
import ca.sheridancollege.khvostiv.beans.User;
import ca.sheridancollege.khvostiv.repositories.PianoPieceRepository;
import ca.sheridancollege.khvostiv.repositories.UserRepository;
import ca.sheridancollege.khvostiv.services.PianoPieceService;

@SpringBootTest
public class PianoPieceServiceIntegrationTests {

	@Autowired
	private PianoPieceService pianoPieceService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PianoPieceRepository pianoPieceRepository;

	@Test
	public void testSavePianoPieceAssignsUser() {

		// Create a test user
		User testUser = new User();
		testUser.setEmail("qa-test-" + System.currentTimeMillis() + "@example.com");
		testUser.setPassword("test-password");
		testUser.setRole(Role.USER);

		testUser = userRepository.save(testUser);

		// Create a piano piece
		PianoPiece piece = new PianoPiece();
		piece.setTitle("QA Service Test Piece");
		piece.setDifficulty("Beginner");

		// Call the business logic
		PianoPiece savedPiece = pianoPieceService.save(piece, testUser.getEmail());

		// Verify the piece was saved
		assertNotNull(savedPiece.getId());

		// Verify the service assigned the correct user
		assertNotNull(savedPiece.getUser());
		assertEquals(testUser.getEmail(), savedPiece.getUser().getEmail());

		// Clean up test data
		pianoPieceRepository.delete(savedPiece);
		userRepository.delete(testUser);
	}
}