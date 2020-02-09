package ca.mcgill.ecse321.petshelter;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.*;
@RunWith(SpringRunner.class)
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

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
//		advertisementRepository.deleteAll();
//		applicationRepository.deleteAll();
//		commentRepository.deleteAll();
//		donationRepository.deleteAll();
//		forumRepository.deleteAll();
		userRepository.deleteAll();
		petRepository.deleteAll();

	}

	@Test
	public void testPersistAndLoadUser() {
		// user info
		String name = "TestUserName";
		String email = "test@email.com";
		User user = new User();
		user.setUserName(name);
		user.setEmail(email);
		user.setApiToken("10");
		user.setApplications(new HashSet<AdoptionApplication>());
		user.setGender(Gender.FEMALE);
		user.setIsEmailValidated(true);
		user.setPassword("123");
		user.setPets(new HashSet<Pet>());
		userRepository.save(user);

		//user = null;

		user = userRepository.findUserById(new Long(0));
		assertNotNull(user);
		assertEquals(name, user.getUserName());
	}

	@Test
	public void testPersistAndLoadPet() {
		// user info
		String name = "TestUserName";
		String email = "test@email.com";
		User user = new User();
		user.setUserName(name);
		user.setEmail(email);
		userRepository.save(user);

		// pet info
		String petName = "TestPetName";
		Date birthDate = Date.valueOf("2015-03-31");
		String species = "Dog";
		String breed = "Labrador";
		Pet pet = new Pet();
		pet.setDateOfBirth(birthDate);
		pet.setName(petName);
		pet.setSpecies(species);
		pet.setBreed(breed);
		Set pets = new HashSet<Pet>();
		user.setPets(pets);
		userRepository.save(user);
		petRepository.save(pet);


		pet = null;

		pet = petRepository.findPetByName(petName);
		assertNotNull(pet);
		assertEquals(petName, pet.getName());

	}
}
