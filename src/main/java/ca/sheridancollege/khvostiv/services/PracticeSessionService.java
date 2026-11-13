package ca.sheridancollege.khvostiv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.beans.PracticeSession;
import ca.sheridancollege.khvostiv.beans.User;
import ca.sheridancollege.khvostiv.models.PracticeSessionRequest;
import ca.sheridancollege.khvostiv.models.PracticeSessionResponse;
import ca.sheridancollege.khvostiv.repositories.PianoPieceRepository;
import ca.sheridancollege.khvostiv.repositories.PracticeSessionRepository;
import ca.sheridancollege.khvostiv.repositories.UserRepository;
import java.time.Duration;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PracticeSessionService {

	private PracticeSessionRepository practiceSessionRepository;
	private PianoPieceRepository pianoPieceRepository;
	private UserRepository userRepository;

	public PracticeSessionResponse save(PracticeSessionRequest request, String email) {

		PianoPiece pianoPiece = pianoPieceRepository.findByIdAndUserEmail(request.getPianoPieceId(), email)
				.orElseThrow();

		User user = userRepository.findByEmail(email).orElseThrow();

		long durationSeconds = Duration.between(request.getStartedAt(), request.getEndedAt()).getSeconds();

		PracticeSession practiceSession = new PracticeSession();

		practiceSession.setStartedAt(request.getStartedAt());
		practiceSession.setEndedAt(request.getEndedAt());
		practiceSession.setDurationSeconds(durationSeconds);
		practiceSession.setNotes(request.getNotes());
		practiceSession.setPianoPiece(pianoPiece);
		practiceSession.setUser(user);

		PracticeSession savedSession = practiceSessionRepository.save(practiceSession);

		return convertToResponse(savedSession);
	}

	public List<PracticeSessionResponse> findAllByUser(String email) {

		List<PracticeSession> practiceSessions = practiceSessionRepository.findByUserEmailOrderByStartedAtDesc(email);

		List<PracticeSessionResponse> responses = new ArrayList<>();

		for (PracticeSession practiceSession : practiceSessions) {

			responses.add(convertToResponse(practiceSession));
		}

		return responses;
	}

	public PracticeSessionResponse findByIdAndUser(Long id, String email) {

		PracticeSession practiceSession = practiceSessionRepository.findByIdAndUserEmail(id, email).orElseThrow();

		return convertToResponse(practiceSession);
	}

	public void delete(Long id, String email) {

		PracticeSession practiceSession = practiceSessionRepository.findByIdAndUserEmail(id, email).orElseThrow();

		practiceSessionRepository.delete(practiceSession);
	}

	private PracticeSessionResponse convertToResponse(PracticeSession practiceSession) {

		PracticeSessionResponse response = new PracticeSessionResponse();
		response.setId(practiceSession.getId());
		response.setPianoPieceId(practiceSession.getPianoPiece().getId());
		response.setPianoPieceTitle(practiceSession.getPianoPiece().getTitle());
		response.setStartedAt(practiceSession.getStartedAt());
		response.setEndedAt(practiceSession.getEndedAt());
		response.setDurationSeconds(practiceSession.getDurationSeconds());
		response.setNotes(practiceSession.getNotes());

		return response;
	}
	
	public List<PracticeSession> findAllEntitiesByUser(String email) {

		return practiceSessionRepository
				.findByUserEmailOrderByStartedAtDesc(email);
	}
}
