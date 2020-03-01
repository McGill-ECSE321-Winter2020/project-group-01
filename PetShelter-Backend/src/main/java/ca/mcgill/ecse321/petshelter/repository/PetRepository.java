package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

/**
 * @author louis
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "pet_data", path = "pet_data")

public interface PetRepository extends CrudRepository<Pet, Long> {
	List<Pet> findPetByName(String name); //there could be many of the same pet name
	
	Pet findPetById(long id);
	
	List<Pet> findPetByAdvertisement(Advertisement advertisement);
	
	Set<Pet> findAll();
	
	Pet findPetByNameAndAdvertisement(String petName, Advertisement advertisement);
	
	//doesnt work  void deletePetsByUserUserName(String username);
}
