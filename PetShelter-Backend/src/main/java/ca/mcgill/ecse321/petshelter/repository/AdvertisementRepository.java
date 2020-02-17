package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "advertisement_data", path = "advertisement_data")
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{

	Advertisement findAdvertisementByTitle(String title);

}
