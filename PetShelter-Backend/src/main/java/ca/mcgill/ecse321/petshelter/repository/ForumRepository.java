package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Forum;

/**
 * @author louis
 *
 */
public interface ForumRepository extends JpaRepository<Forum, Long>{

}
