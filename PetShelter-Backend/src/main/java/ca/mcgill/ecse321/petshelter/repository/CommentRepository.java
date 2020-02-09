package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

	Comment findCommentByUserAndText(User user, String commentText);

}
