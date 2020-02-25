package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Louis
 * @author Katrina
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "user_data", path = "user_data")
public interface UserRepository extends CrudRepository<User, Long>{
	
	User findUserByUserName(String name);
	User findUserByEmail(String email);
	User findUserByApiToken(String token);
	
}
