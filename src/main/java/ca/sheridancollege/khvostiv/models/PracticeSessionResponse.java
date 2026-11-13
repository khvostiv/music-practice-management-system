package ca.sheridancollege.khvostiv.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
public class PracticeSessionResponse {
	
    private Long id;
    private Long pianoPieceId;
    private String pianoPieceTitle;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long durationSeconds;
    private String notes;
}
