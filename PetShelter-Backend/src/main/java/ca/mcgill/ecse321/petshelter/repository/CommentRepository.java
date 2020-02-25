package ca.mcgill.ecse321.petshelter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "comment_data", path = "comment_data")
public interface CommentRepository extends JpaRepository<Comment, Long>{

	Comment findCommentByUserAndText(User user, String commentText);

	Comment findCommentById(long id);

	List<Comment> findCommentsByUser(User user);

}
