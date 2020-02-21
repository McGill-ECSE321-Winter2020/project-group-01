package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.petshelter.model.*;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "application_data", path = "application_data")
public interface ApplicationRepository extends JpaRepository<AdoptionApplication, Long>{

	AdoptionApplication findApplicationByUserAndAdvertisement(User applicant, Advertisement advertisement);
}
