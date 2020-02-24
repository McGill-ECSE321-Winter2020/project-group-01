package ca.mcgill.ecse321.petshelter.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.MockitoJUnitRunner;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyString;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("deprecation")
public class TestPetService {

	@Mock
	private PetRepository petDao;
	
	@Mock
	private UserRepository userDao;
	
	@InjectMocks
	private UserService userService;
	
	@InjectMocks
	private PetService petService;
	
	//User parameters
	private static final String USER_NAME = "testUN";
	private static final String USER_EMAIL = "username@gmail.com";
	private static final String USER_PASSWORD = "testUN";
	private static final UserType USER_TYPE = UserType.USER;
	
	//Pet parameters
	private static final Date PET_DOB = new Date(119, 10, 20);
	private static final String PET_NAME = "testPettt";
	private static final String PET_SPECIES = "testSpecies";
	private static final String PET_BREED = "testBreed";
	private static final String PET_DESCRIPTION = "testDesc";
	private static final byte[] PET_PICTURE = new byte[10];
	private static final Gender PET_GENDER = Gender.FEMALE;
	private static final Set<Pet> USER_PETS = new HashSet<Pet>();
	private long petId;
	private Pet pet;

	
	//TODO this is not called
	@AfterEach
	@BeforeEach
	public void clearDatabase() {
		userDao.deleteAll();
		petDao.deleteAll();
	}
	
	@Before
	public void setMockOutput() {
		//user mock
		lenient().when(userDao.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if(invocation.getArgument(0).equals(USER_NAME)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				user.setUserType(USER_TYPE);
				user.setPets(USER_PETS);
				
				return user;
			} else {
				return null;
			}
		});
		
		lenient().when(petDao.findPetById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if(invocation.getArgument(0).equals((int) petId)) {
				return pet;
			} else {
				return null;
			}
		});
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(userDao.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(petDao.save(any(Pet.class))).thenAnswer(returnParameterAsAnswer);
		
	}
	/**
	 * Creates a test user in the database.
	 * @author Katrina
	 * @return user created
	 */
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
        userDao.save(user);
        
        return user;
    }
	
	/**
	 * Normal test case. Creates a pet. Should not throw any exception".
	 * @author Katrina
	 */
	@Test
	public void testCreatePet() {
    	//clearDatabase();
		//User user = createUser();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		Date DOB = new Date (119, 10, 20);
		byte[] pic = new byte[10];
		pet = petService.createPet(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		petDao.save(pet);
		petId = pet.getId();
		assertNotNull(pet);
		System.err.println(petDao.findAll());
 		//assertNotNull(petDao.findPetById(petId));
		assertEquals("testPettt",pet.getName());
		assertEquals(new Date(119, 10, 20), DOB);
		assertEquals("testSpecies", pet.getSpecies());
		assertEquals("testBreed", pet.getBreed());
		assertEquals("testDesc", pet.getDescription());
		assertEquals(PET_PICTURE, pet.getPicture());
		User fetchedUser = userDao.findUserByUserName(USER_NAME);
		Pet fetchedPet = (Pet) fetchedUser.getPets().toArray()[0];
		assertTrue(fetchedPet.getId() == petId);
		assertEquals(Gender.FEMALE, pet.getGender());	
	}
	
	
	/**
	 * Creates a pet with no user. Expects IllegalArgumentException with
	 * message "User does not exist: a pet needs a user".
	 * @author Katrina
	 */

	@Test
	public void testCreatePetNoUser() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			pet = petService.createPet(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, "", PET_GENDER);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "User does not exist: a pet needs a user");
		}
		assertEquals(pet, null);
	}

	/**
	 * Creates a pet with no name. Expects IllegalArgumentException with
	 * message "A pet needs a name".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoName() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			pet = petService.createPet(PET_DOB, "", PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "A pet needs a name");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no species. Expects IllegalArgumentException with
	 * message "A pet needs a species".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoSpecies() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			pet = petService.createPet(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "A pet needs a species");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no breed. Expects IllegalArgumentException with
	 * message "A pet needs a breed".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoBreed() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			pet = petService.createPet(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "A pet needs a breed");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoDesc() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		pet = petService.createPet(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, "", PET_PICTURE, USER_NAME, PET_GENDER);
		assertNotNull(pet);
		assertEquals("", pet.getDescription());
	}
	
	/**
	 * Creates a pet with no gender. Expects IllegalArgumentException with
	 * message "A pet needs a gender".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoGender() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			pet = petService.createPet(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, null);
		} catch(IllegalArgumentException e) {
			assertEquals(e.getMessage(), "A pet needs a gender");
		}
		assertEquals(pet, null);
	}
	//TODO two owners one pet while editing
  }
