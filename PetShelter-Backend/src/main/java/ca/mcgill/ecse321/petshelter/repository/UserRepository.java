package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author Louis
 * @author Katrina
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{
	User findUserById(Long id);
	User findUserByUserName(String name);
}
