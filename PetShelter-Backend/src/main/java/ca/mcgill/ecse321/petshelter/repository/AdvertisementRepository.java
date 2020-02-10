package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author louis
 *
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{

	Advertisement findAdvertisementByTitle(String title);

}
