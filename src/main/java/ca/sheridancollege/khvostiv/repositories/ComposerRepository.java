package ca.sheridancollege.khvostiv.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.khvostiv.beans.Composer;

@Repository
public interface ComposerRepository extends JpaRepository<Composer, Long> {

	Optional<Composer> findByName(String name);

}
