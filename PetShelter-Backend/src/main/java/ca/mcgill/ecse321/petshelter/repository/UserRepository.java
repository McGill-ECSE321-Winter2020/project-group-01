package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Louis
 * @author Katrina
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{
	User findUserById(Long id);
	User findUserByUserName(String name);
}
