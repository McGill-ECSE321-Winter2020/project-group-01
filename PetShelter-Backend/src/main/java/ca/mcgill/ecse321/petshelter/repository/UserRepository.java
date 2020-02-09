package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{

	User findUserByUserName(String name);
}
