package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "forum_data", path = "forum_data")
public interface ForumRepository extends JpaRepository<Forum, Long>{

	Forum findForumByTitle(String title);
	
	//List<Forum> findAllByUser(User user); // ADDED THIS. NEED TO ADD CORRESPONDING TEST CASE.

	
}
