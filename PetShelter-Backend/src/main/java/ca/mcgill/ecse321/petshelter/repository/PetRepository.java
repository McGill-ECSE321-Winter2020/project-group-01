package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.petshelter.model.Pet;

/**
 * @author louis
 *
 */
public interface PetRepository extends CrudRepository<Pet, Long>{

	Pet findPetByName(String name);
}
