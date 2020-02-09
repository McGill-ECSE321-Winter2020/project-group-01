package ca.mcgill.ecse321.petshelter.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.PetShelterApplication;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class PetShelterPersistence {
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
		advertisementRepository.deleteAll();
		applicationRepository.deleteAll();
		commentRepository.deleteAll();
		donationRepository.deleteAll();
		forumRepository.deleteAll();
		userRepository.deleteAll();
		petRepository.deleteAll();

	}
	
	@BeforeEach
	public User createUser() {
		String name = "TestUserName";
		String password = "myPassword";
		boolean emailValid = true;
		String email = "TestUserName@gmail.com";
		String apiToken = "token112";

		User user = new User();
		user.setUserName(name);
		user.setPassword(password);
		user.setIsEmailValidated(emailValid);
		user.setEmail(email);
		user.setApiToken(apiToken);

		userRepository.save(user);
		return user;
	}
	
	@BeforeEach
	public Advertisement createAdvertisement() {
		String description = "myDescription";
		String title = "myTitle";
		long id = 12345;
		boolean isFulfiled = true;

		Advertisement advertisement = new Advertisement();

		advertisement.setDescription(description);
		advertisement.setId(id);
		advertisement.setIsFulfilled(isFulfiled);
		// advertisement.setTitle(title);

		advertisementRepository.save(advertisement);
		return advertisement;
	}

	@Test
	public void testPersistAndLoadUser() {
		String name = "TestUserName";
		String password = "myPassword";
		boolean emailValid = true;
		String email = "TestUserName@gmail.com";
		String apiToken = "token112";

		User user = new User();

		// sets everything
		user.setUserName(name);
		user.setPassword(password);
		user.setIsEmailValidated(emailValid);
		user.setEmail(email);
		user.setApiToken(apiToken);

		userRepository.save(user);

		user = null;

		user = userRepository.findUserByUserName(name);
		assertNotNull(user);
		assertEquals(name, user.getUserName());
		assertEquals(password, user.getPassword());
		assertEquals(emailValid, user.isIsEmailValidated());
		assertEquals(apiToken, user.getApiToken());
	}
	
	@Test
	public void testPersistAndLoadComment() {
		Date postedDate = new Date(123);
		String commentText = "this is a comment";
		Time time = new Time(111);
		
		User user = createUser();
		Comment comment = new Comment();
		
		comment.setDatePosted(postedDate);
		comment.setText(commentText);
		comment.setTime(time);
		comment.setUser(user);
		
		commentRepository.save(comment);
		
		comment = null;
		comment = commentRepository.findCommentByUserAndText(user, commentText);
		
		assertNotNull(comment);
		assertEquals(commentText,comment.getText());
		
	}
	
//TODO: doesnt work
//	@Test
//	public void testPersistAndLoadAdvertisement() {
//		String description = "myDescription";
//		//String title = "myTitle";
//		long id = 12345L;
//		boolean isFulfiled = true;
//
//		Advertisement advertisement = new Advertisement();
//
//		advertisement.setDescription(description);
//		advertisement.setId(id);
//		advertisement.setIsFulfilled(isFulfiled);
//		// advertisement.setTitle(title);
//
//		advertisementRepository.save(advertisement);
//
//		advertisement = advertisementRepository.findAdverstisementById(id);
//		assertNotNull(advertisement);
//		// assertEquals(title, advertisement.getTitle());
//		assertEquals(id, advertisement.getId());
//		assertEquals(isFulfiled, advertisement.isIsFulfilled());
//	}

	
//	
//	@Test
//	public void testPersistAndLoadApplication() {
//		User applicant = createUser();
//		String description = "myDescription";
//		Advertisement advertisement = createAdvertisement();
//		boolean isAccepted = false;
//		long id = 123423L;
//
//		AdoptionApplication application = new AdoptionApplication();
//		application.setIsAccepted(isAccepted);
//		application.setAdvertisement(advertisement);
//		// application.setApplicant(applicant);
//		application.setDescription(description);
//		application.setId(id);
//
//		applicationRepository.save(application);
//
//		application = applicationRepository.finApplicationByUserAndAdvertisement(applicant, advertisement);
//		assertNotNull(application);
//		// assertEquals(applicant,application.getApplicant());
//		assertEquals(advertisement, application.getAdvertisement());
//		assertEquals(description, application.getDescription());
//		assertEquals(isAccepted, application.isIsAccepted());
//	}

	@Test
	public void testPersistAndLoadPet() {
		// user info
		User user = createUser();

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
		pet.setGender(Gender.FEMALE);
		HashSet<Pet> pets = new HashSet<Pet>();
		user.setPets(pets);
		userRepository.save(user);
		petRepository.save(pet);

		pet = null;

		pet = petRepository.findPetByName(petName);
		assertNotNull(pet);
		assertEquals(petName, pet.getName());

	}
}
