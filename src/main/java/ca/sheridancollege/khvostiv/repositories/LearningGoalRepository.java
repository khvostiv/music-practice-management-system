package ca.sheridancollege.khvostiv.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.khvostiv.beans.LearningGoal;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal, Long> {

	List<LearningGoal> findByUserEmailOrderByDeadlineAsc(String email);
	Optional<LearningGoal> findByIdAndUserEmail(Long id, String email);
	
	void deleteByPianoPiece_Id(Long pianoPieceId);
	
}