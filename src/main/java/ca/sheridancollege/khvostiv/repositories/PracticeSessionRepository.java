package ca.sheridancollege.khvostiv.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.khvostiv.beans.PracticeSession;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {

	List<PracticeSession> findByUserEmailOrderByStartedAtDesc(String email);
	Optional<PracticeSession> findByIdAndUserEmail(Long id, String email);
	
	void deleteByPianoPiece_Id(Long pianoPieceId);
	
}
