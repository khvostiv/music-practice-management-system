package ca.sheridancollege.khvostiv.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.khvostiv.beans.PianoPiece;

@Repository
public interface PianoPieceRepository extends JpaRepository<PianoPiece, Long> {

	List<PianoPiece> findByUserEmail(String email);

	Optional<PianoPiece> findByIdAndUserEmail(Long id, String email);
}
