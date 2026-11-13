package ca.sheridancollege.khvostiv.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.beans.User;
import ca.sheridancollege.khvostiv.repositories.LearningGoalRepository;
import ca.sheridancollege.khvostiv.repositories.PianoPieceRepository;
import ca.sheridancollege.khvostiv.repositories.PracticeSessionRepository;
import ca.sheridancollege.khvostiv.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PianoPieceService {

	private PianoPieceRepository pianoPieceRepository;
	private UserRepository userRepository;
	private LearningGoalRepository learningGoalRepository;
	private PracticeSessionRepository practiceSessionRepository;

	public List<PianoPiece> findAllByUser(String email) {

		return pianoPieceRepository.findByUserEmail(email);
	}

	public PianoPiece findByIdAndUser(Long id, String email) {

		return pianoPieceRepository.findByIdAndUserEmail(id, email).orElseThrow();
	}

	public PianoPiece save(PianoPiece pianoPiece, String email) {

		User user = userRepository.findByEmail(email).orElseThrow();
		pianoPiece.setUser(user);

		return pianoPieceRepository.save(pianoPiece);
	}

	public PianoPiece update(Long id, PianoPiece updatedPiece, String email) {

		PianoPiece existingPiece = findByIdAndUser(id, email);

		existingPiece.setTitle(updatedPiece.getTitle());
		existingPiece.setComposer(updatedPiece.getComposer());
		existingPiece.setGenre(updatedPiece.getGenre());
		existingPiece.setDifficulty(updatedPiece.getDifficulty());
		existingPiece.setLearningStatus(updatedPiece.getLearningStatus());
		existingPiece.setRating(updatedPiece.getRating());
		existingPiece.setSheetMusicFileName(updatedPiece.getSheetMusicFileName());
		existingPiece.setSheetMusicPath(updatedPiece.getSheetMusicPath());

		return pianoPieceRepository.save(existingPiece);
	}

	@Transactional
	public PianoPiece delete(Long id, String email) {

		PianoPiece pianoPiece = findByIdAndUser(id, email);

		learningGoalRepository.deleteByPianoPiece_Id(id);
		practiceSessionRepository.deleteByPianoPiece_Id(id);
		pianoPieceRepository.delete(pianoPiece);

		return pianoPiece;
	}
}