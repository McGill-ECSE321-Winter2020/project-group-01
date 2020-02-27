package ca.mcgill.ecse321.petshelter.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
/**
 * @author louis
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "pet_data", path = "pet_data")

public interface PetRepository extends CrudRepository<Pet, Long>{
	Pet findPetByName(String name);
	Pet findPetById(long id);
	List<Pet> findPetByAdvertisement(Advertisement advertisement);
	Set<Pet> findAll();
}
