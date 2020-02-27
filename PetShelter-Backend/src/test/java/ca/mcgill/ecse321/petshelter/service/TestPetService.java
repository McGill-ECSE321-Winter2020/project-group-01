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

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

//TODO Javadoc
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
		
		lenient().when(petDao.findPetById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
			if(invocation.getArgument(0).equals(petId)) {
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
	
	//TODO are we doing this?
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
	
    
////////////////////////////// CREATE PET //////////////////////////////

	/**
	 * Normal test case. Creates a pet. Should not throw any exception".
	 * @author Katrina
	 */
	@Test
	public void testCreatePet() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		Date DOB = new Date (119, 10, 20);
		pet = petService.createPet(petDto);
		petId = pet.getId();
		assertNotNull(pet);
		assertNotNull(petDao.findPetById(petId));
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
	 * Creates a pet with no user. Expects PetException with
	 * message "User does not exist: a pet needs a user".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoUser() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, "", PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: User does not exist.");
		}
		assertEquals(pet, null);
	}

	/**
	 * Creates a pet with no name. Expects PetException with
	 * message "A pet needs a name".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoName() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, "", PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a name.");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no species. Expects PetException with
	 * message "A pet needs a species".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoSpecies() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a species.");
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
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a breed.");
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
		PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, "", PET_PICTURE, USER_NAME, PET_GENDER);
		pet = petService.createPet(petDto);
		assertNotNull(pet);
		assertEquals("", pet.getDescription());
		assertNotNull(petDao.findPetById(pet.getId()));
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
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, null);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a gender.");
		}
		assertEquals(pet, null);
	}
	
////////////////////////////// EDIT PET //////////////////////////////
//TODO everything here
	/**
	 * Normal test case. Creates a pet. Should not throw any exception".
	 * @author Katrina
	 */
	@Test
	public void testEditPet() {
		assertEquals(0, petService.getAllPets().size());
		
		pet = null;
		PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
		Date DOB = new Date (119, 10, 20);
		pet = petService.createPet(petDto);
		petId = pet.getId();
		assertNotNull(pet);
		assertNotNull(petDao.findPetById(petId));
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
		
		petDto.setName("newName");
		petDto.setSpecies("newSpecies");
		petDto.setBreed("newBreed");
		petDto.setDescription("newDesc");
		byte[] newPic = new byte[10];
		petDto.setPicture(newPic);
		petDto.setGender(Gender.MALE);
		pet = petService.editPet(petDto);
		assertNotNull(pet);
		assertNotNull(petDao.findPetById(petId));
		assertEquals("newName",pet.getName());
		assertEquals(PET_DOB, pet.getDateOfBirth());
		assertEquals("newSpecies", pet.getSpecies());
		assertEquals("newBreed", pet.getBreed());
		assertEquals("newDesc", pet.getDescription());
		assertEquals(newPic, pet.getPicture());
		fetchedUser = userDao.findUserByUserName(USER_NAME);
		fetchedPet = (Pet) fetchedUser.getPets().toArray()[0];
		assertTrue(fetchedPet.getId() == petId);
		assertEquals(Gender.MALE, pet.getGender());	
	}

	/**
	 * Creates a pet with no user. Expects PetException with
	 * message "User does not exist: a pet needs a user".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoUser() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, "", PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: User does not exist.");
		}
		assertEquals(pet, null);
	}

	/**
	 * Creates a pet with no name. Expects PetException with
	 * message "A pet needs a name".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoName() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, "", PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a name.");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no species. Expects PetException with
	 * message "A pet needs a species".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoSpecies() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a species.");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no breed. Expects IllegalArgumentException with
	 * message "A pet needs a breed".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoBreed() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a breed.");
		}
		assertEquals(pet, null);
	}
	
	/**
	 * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoDesc() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, "", PET_PICTURE, USER_NAME, PET_GENDER);
		pet = petService.createPet(petDto);
		assertNotNull(pet);
		assertEquals("", pet.getDescription());
		assertNotNull(petDao.findPetById(pet.getId()));
	}
	
	/**
	 * Creates a pet with no gender. Expects IllegalArgumentException with
	 * message "A pet needs a gender".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoGender() {
		clearDatabase();
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, null);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a gender.");
		}
		assertEquals(pet, null);
	}
	
	@Test
	public void testChangeOwnership() {
		//TODO
	}
	private PetDTO createPetDto(Date petDob, String petName, String petSpecies, String petBreed, String petDescription,
			byte[] petPicture, String userName, Gender petGender) {
		PetDTO dto = new PetDTO(petDob, petName, petSpecies, petBreed, petDescription, petPicture, petGender, null, userName);
		return dto;
	}
  }
