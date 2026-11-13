package ca.sheridancollege.khvostiv.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoalResponse {

	private Long id;
	private Long pianoPieceId;
	private String pianoPieceTitle;
	private Integer targetMinutes;
	private Long practicedMinutes;
	private LocalDate deadline;
	private Boolean completed;
}