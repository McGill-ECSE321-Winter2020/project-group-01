package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;

/**
 * @author louis
 *
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "donation_data", path = "donation_data")
public interface DonationRepository extends JpaRepository<Donation, Long>{

	Donation findDonationByUserAndAmount(User user, double amount);

}
