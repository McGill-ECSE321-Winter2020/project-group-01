package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
public interface DonationRepository extends JpaRepository<Donation, Long>{

	Donation findDonationByUserAndAmount(User user, double amount);

}
