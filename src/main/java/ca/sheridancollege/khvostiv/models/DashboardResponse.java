package ca.sheridancollege.khvostiv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

	private Integer totalPianoPieces;
	private Integer totalPracticeSessions;
	private Long totalPracticeMinutes;
	private Integer activeGoals;
	private Integer completedGoals;
	private String mostPracticedPiece;
}