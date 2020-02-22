package ca.mcgill.ecse321.petshelter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.HashSet;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPetService {

	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PetService petService;
	
	@BeforeEach
	public void clearDatabase() {
		petRepository.deleteAll();
	}
	
    public User createUser() {
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
        
        return user;
    }
	@SuppressWarnings("deprecation")
	
	@Test
	public void testCreatePet() {
		assertEquals(0, petService.getAllPets().size());
		User user = createUser();
		Pet pet = null;
		Date DOB = new Date (2019, 10, 20);
		byte[] pic = new byte[10];
		pet = petService.createPet(DOB, "doggy", "TestSpecies", "TestBreed", "TestDesc", pic, user.getUserName(), Gender.FEMALE);
		long petId = pet.getId();
		assertNotNull(pet);
		assertNotNull(petRepository.findPetById(petId));
		assertEquals("doggy",pet.getName());
		assertEquals(new Date(2019, 10, 20), DOB);
		assertEquals("TestSpecies", pet.getSpecies());
		assertEquals("TestBreed", pet.getBreed());
		assertEquals("TestDesc", pet.getDescription());
		assertEquals(pic, pet.getPicture());
		User fetchedUser = userRepository.findUserByUserName(user.getUserName());
		Pet fetchedPet = (Pet) fetchedUser.getPets().toArray()[0];
		assertTrue(fetchedPet.getId() == petId);
		assertEquals(Gender.FEMALE, pet.getGender());	
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testCreatePetNoUser() {
		assertEquals(0, petService.getAllPets().size());
		Pet pet = null;
		Date DOB = new Date (2019, 10, 20);
		byte[] pic = new byte[10];
		try{
			pet = petService.createPet(DOB, "doggy", "TestSpecies", "TestBreed", "TestDesc", pic, "", Gender.FEMALE);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "User does not exist: a pet needs a user");
		}
		assertEquals(pet, null);
	}
	//no all attributes
}
