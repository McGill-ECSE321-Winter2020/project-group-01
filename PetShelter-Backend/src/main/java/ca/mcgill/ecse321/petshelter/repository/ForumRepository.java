package ca.mcgill.ecse321.petshelter.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
public interface ForumRepository extends JpaRepository<Forum, Long>{

	//TODO: this is breaking the tests
//	Forum findForumByUserSetAndCommentSet(Set<User> userSet, Set<Comment> commentSet);

}
