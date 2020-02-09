package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Advertisement;

/**
 * @author louis
 *
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{

	Advertisement findAdvertisementByName(String title);

}
