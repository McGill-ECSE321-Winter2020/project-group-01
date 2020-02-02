package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Donation;

/**
 * @author louis
 *
 */
public interface DonationRepository extends JpaRepository<Donation, Long>{

}
