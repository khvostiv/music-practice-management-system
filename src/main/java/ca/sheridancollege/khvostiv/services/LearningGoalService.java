package ca.sheridancollege.khvostiv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ca.sheridancollege.khvostiv.beans.LearningGoal;
import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.beans.PracticeSession;
import ca.sheridancollege.khvostiv.beans.User;
import ca.sheridancollege.khvostiv.models.LearningGoalRequest;
import ca.sheridancollege.khvostiv.models.LearningGoalResponse;
import ca.sheridancollege.khvostiv.repositories.LearningGoalRepository;
import ca.sheridancollege.khvostiv.repositories.PianoPieceRepository;
import ca.sheridancollege.khvostiv.repositories.PracticeSessionRepository;
import ca.sheridancollege.khvostiv.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LearningGoalService {

	private LearningGoalRepository learningGoalRepository;
	private PianoPieceRepository pianoPieceRepository;
	private PracticeSessionRepository practiceSessionRepository;
	private UserRepository userRepository;

	public LearningGoalResponse save(LearningGoalRequest request, String email) {

		PianoPiece pianoPiece = pianoPieceRepository.findByIdAndUserEmail(request.getPianoPieceId(), email)
				.orElseThrow();

		User user = userRepository.findByEmail(email).orElseThrow();

		LearningGoal learningGoal = new LearningGoal();

		learningGoal.setTargetMinutes(request.getTargetMinutes());
		learningGoal.setDeadline(request.getDeadline());
		learningGoal.setCompleted(false);
		learningGoal.setPianoPiece(pianoPiece);
		learningGoal.setUser(user);

		LearningGoal savedGoal = learningGoalRepository.save(learningGoal);

		List<PracticeSession> practiceSessions = practiceSessionRepository.findByUserEmailOrderByStartedAtDesc(email);

		return convertToResponse(savedGoal, practiceSessions);
	}

	public List<LearningGoalResponse> findAllByUser(String email) {

		List<LearningGoal> learningGoals = learningGoalRepository.findByUserEmailOrderByDeadlineAsc(email);

		List<PracticeSession> practiceSessions = practiceSessionRepository.findByUserEmailOrderByStartedAtDesc(email);

		List<LearningGoalResponse> responses = new ArrayList<>();

		for (LearningGoal learningGoal : learningGoals) {

			responses.add(convertToResponse(learningGoal, practiceSessions));
		}

		return responses;
	}

	public void complete(Long id, String email) {

		LearningGoal learningGoal = learningGoalRepository.findByIdAndUserEmail(id, email).orElseThrow();

		learningGoal.setCompleted(true);

		learningGoalRepository.save(learningGoal);
	}

	public void delete(Long id, String email) {

		LearningGoal learningGoal = learningGoalRepository.findByIdAndUserEmail(id, email).orElseThrow();

		learningGoalRepository.delete(learningGoal);
	}

	private LearningGoalResponse convertToResponse(LearningGoal learningGoal, List<PracticeSession> practiceSessions) {

		long practicedSeconds = 0;

		for (PracticeSession practiceSession : practiceSessions) {

			if (practiceSession.getPianoPiece().getId().equals(learningGoal.getPianoPiece().getId())) {

				practicedSeconds += practiceSession.getDurationSeconds();
			}
		}

		long practicedMinutes = practicedSeconds / 60;

		if (practicedMinutes >= learningGoal.getTargetMinutes() && !Boolean.TRUE.equals(learningGoal.getCompleted())) {

			learningGoal.setCompleted(true);

			learningGoalRepository.save(learningGoal);
		}

		LearningGoalResponse response = new LearningGoalResponse();

		response.setId(learningGoal.getId());
		response.setPianoPieceId(learningGoal.getPianoPiece().getId());
		response.setPianoPieceTitle(learningGoal.getPianoPiece().getTitle());
		response.setTargetMinutes(learningGoal.getTargetMinutes());
		response.setPracticedMinutes(practicedMinutes);
		response.setDeadline(learningGoal.getDeadline());
		response.setCompleted(learningGoal.getCompleted());

		return response;
	}
}