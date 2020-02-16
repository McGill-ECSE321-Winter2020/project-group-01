package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author louis
 *
 */
public interface ForumRepository extends JpaRepository<Forum, Long>{

	//TODO: this is breaking the tests
	//Forum findForumByUserSetAndCommentSet(Set<User> userSet, Set<Comment> commentSet);

	Forum findForumByTitle(String title);

}
