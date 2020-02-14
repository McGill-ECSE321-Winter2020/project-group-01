package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "forum_data", path = "forum_data")
public interface ForumRepository extends JpaRepository<Forum, Long>{

	//TODO: this is breaking the tests
	//Forum findForumByUserSetAndCommentSet(Set<User> userSet, Set<Comment> commentSet);

	Forum findForumByTitle(String title);

}
