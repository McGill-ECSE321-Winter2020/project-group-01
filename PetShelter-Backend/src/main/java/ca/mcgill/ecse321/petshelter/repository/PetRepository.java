package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author louis
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "pet_data", path = "pet_data")
public interface PetRepository extends CrudRepository<Pet, Long> {
    
    Pet findPetByNameAndAdvertisement(String name, Advertisement advertisement);
    //List<Pet> findAllByUser(User user); //ADDED THIS. NEED TO ADD CORRESPONDING TEST CASE.
}
