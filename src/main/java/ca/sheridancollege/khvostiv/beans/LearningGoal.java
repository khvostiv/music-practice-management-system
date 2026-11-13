package ca.sheridancollege.khvostiv.beans;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "learning_goal")
public class LearningGoal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer targetMinutes;

	private LocalDate deadline;

	private Boolean completed;

	@ManyToOne
	@JoinColumn(name = "piano_piece_id")
	private PianoPiece pianoPiece;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}