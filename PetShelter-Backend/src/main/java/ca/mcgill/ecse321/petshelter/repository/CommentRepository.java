package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author louis
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "comment_data", path = "comment_data")
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	Comment findCommentByUserAndText(User user, String commentText);
	
	List<Comment> findCommentsByUser(User user);
	
}
