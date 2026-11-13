package ca.sheridancollege.khvostiv.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.beans.PracticeSession;
import ca.sheridancollege.khvostiv.models.DashboardResponse;
import ca.sheridancollege.khvostiv.models.LearningGoalResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DashboardService {

	private PianoPieceService pianoPieceService;
	private PracticeSessionService practiceSessionService;
	private LearningGoalService learningGoalService;

	public DashboardResponse getDashboard(String email) {

		List<PianoPiece> pianoPieces = pianoPieceService.findAllByUser(email);

		List<PracticeSession> practiceSessions = practiceSessionService.findAllEntitiesByUser(email);

		List<LearningGoalResponse> learningGoals = learningGoalService.findAllByUser(email);

		long totalSeconds = 0;

		Map<Long, Long> practiceTimeByPiece = new HashMap<>();
		Map<Long, String> pieceTitles = new HashMap<>();

		for (PracticeSession practiceSession : practiceSessions) {

			totalSeconds += practiceSession.getDurationSeconds();

			Long pieceId = practiceSession.getPianoPiece().getId();
			String pieceTitle = practiceSession.getPianoPiece().getTitle();

			practiceTimeByPiece.put(pieceId,
					practiceTimeByPiece.getOrDefault(pieceId, 0L) + practiceSession.getDurationSeconds());

			pieceTitles.put(pieceId, pieceTitle);
		}

		int activeGoals = 0;
		int completedGoals = 0;

		for (LearningGoalResponse learningGoal : learningGoals) {

			if (Boolean.TRUE.equals(learningGoal.getCompleted())) {
				completedGoals++;
			} else {
				activeGoals++;
			}
		}

		String mostPracticedPiece = "No practice sessions yet";
		long highestPracticeTime = 0;

		for (Long pieceId : practiceTimeByPiece.keySet()) {

			long practiceTime = practiceTimeByPiece.get(pieceId);

			if (practiceTime > highestPracticeTime) {
				highestPracticeTime = practiceTime;
				mostPracticedPiece = pieceTitles.get(pieceId);
			}
		}

		DashboardResponse response = new DashboardResponse();

		response.setTotalPianoPieces(pianoPieces.size());
		response.setTotalPracticeSessions(practiceSessions.size());
		response.setTotalPracticeMinutes(totalSeconds / 60);
		response.setActiveGoals(activeGoals);
		response.setCompletedGoals(completedGoals);
		response.setMostPracticedPiece(mostPracticedPiece);

		return response;
	}
}