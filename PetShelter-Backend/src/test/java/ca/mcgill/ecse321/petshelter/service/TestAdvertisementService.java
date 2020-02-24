package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAdvertisementService {

	@Autowired
	AdvertisementService advertisementService;
	
	@Autowired
	PetService petService;
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void clearDatabase() {
		userRepository.deleteAll();
		advertisementRepository.deleteAll();
		petRepository.deleteAll();
	}
	//TODO
	/**
	 * Creates a test user and pet in the database.
	 * @author Katrina
	 */
	public void createUserAndPet() {

	        User user = new User();
	        String userName = "testUN";
	        String password = "myPassword1!";
	        String email = "TestUserName@gmail.com";
	        UserType userType = UserType.USER;
	        
	        user.setUserName(userName);
	        user.setUserType(userType);
	        user.setPassword(password);
	        user.setEmail(email);
	        userRepository.save(user);
	        byte [] pic = new byte [10];
	        petService.createPet(new Date(119, 10, 20), "TestPet", "TestSpecies", "TestBreed", "TestDesc",pic, user.getUserName(), Gender.MALE);
	        
	    }

	@Test
	public void testCreateAdvertisement() {
		createUserAndPet();
		
	}
	@Test
	public void testEmptyTitle() {
		createUserAndPet();

	}
	/*TESTS
	 * - empty all args
	 * - null pet
	 * - second ad for this pet
	 */
}
