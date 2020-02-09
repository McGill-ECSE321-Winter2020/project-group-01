package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Advertisement;

/**
 * @author louis
 *
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{

	
//	TODO: implement find my title
	//investigate why the fuck it crashes the tests these 2
	
//	Advertisement findAdverstisementById(long id);

//	Advertisement findAdvertisementByTitle(String title);

}
