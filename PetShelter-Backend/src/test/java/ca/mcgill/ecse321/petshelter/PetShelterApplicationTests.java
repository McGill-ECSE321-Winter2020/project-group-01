package ca.mcgill.ecse321.petshelter;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.*;

@SpringBootTest
class PetShelterApplicationTests {

	@Autowired
	private AdvertisementRepository advertisementRepository;
	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private DonationRepository donationRepository;
	@Autowired
	private ForumRepository forumRepository;
	@Autowired
	private PetRepository petRepository;
	@Autowired
	private UserRepository userRepository;
	
	@AfterEach
	public void clearDatabase() {
		advertisementRepository.deleteAll();
		applicationRepository.deleteAll();
		commentRepository.deleteAll();
		donationRepository.deleteAll();
		forumRepository.deleteAll();
		petRepository.deleteAll();
		userRepository.deleteAll();
	}
	@Test
	public void testPersistAndLoadUser() {
		String name = "TestUserName";
		User user = new User();
		user.setUserName(name);
		userRepository.save(user);
		
		user = null;
		
		user = userRepository.findUserByName(name);
		assertNotNull(user);
		assertEquals(name, user.getUserName());	
	}
}
