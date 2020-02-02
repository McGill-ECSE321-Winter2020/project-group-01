package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Comment;

/**
 * @author louis
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
