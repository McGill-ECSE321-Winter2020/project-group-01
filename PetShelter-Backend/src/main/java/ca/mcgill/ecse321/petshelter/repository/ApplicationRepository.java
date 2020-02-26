package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "application_data", path = "application_data")
public interface ApplicationRepository extends JpaRepository<AdoptionApplication, Long>{

	AdoptionApplication findApplicationByUserAndAdvertisement(User applicant, Advertisement advertisement);
	
	//List<AdoptionApplication> findAllByUser(User user); //ADDED THIS (NEED TO ADD CORRESPONDING TEST)
}
