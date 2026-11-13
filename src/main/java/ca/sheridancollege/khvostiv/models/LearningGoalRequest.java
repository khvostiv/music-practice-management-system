package ca.sheridancollege.khvostiv.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoalRequest {

	private Long pianoPieceId;
	private Integer targetMinutes;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate deadline;
}