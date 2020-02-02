package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Pet;

/**
 * @author louis
 *
 */
public interface PetRepository extends JpaRepository<Pet, Long>{

}
