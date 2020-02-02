package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{

}
