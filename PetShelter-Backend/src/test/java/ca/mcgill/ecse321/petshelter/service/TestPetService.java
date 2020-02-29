package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.PetException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

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
    
    
    @BeforeEach
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
        assertEquals(fetchedPet.getId(), petId);
        assertEquals(Gender.FEMALE, pet.getGender());
	}

	/**
	 * Creates a pet with no user. Expects PetException with
	 * message "User does not exist: a pet needs a user".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoUser() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, "", PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: User does not exist.");
		}
        assertNull(pet);
	}

	/**
	 * Creates a pet with no name. Expects PetException with
	 * message "A pet needs a name".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoName() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, "", PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a name.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no species. Expects PetException with
	 * message "A pet needs a species".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoSpecies() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a species.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no breed. Expects IllegalArgumentException with
	 * message "A pet needs a breed".
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoBreed() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a breed.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
	 * @author Katrina
	 */
	@Test
	public void testCreatePetNoDesc() {
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
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, null);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a gender.");
		}
        assertNull(pet);
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
        assertEquals(fetchedPet.getId(), petId);
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
        assertEquals(fetchedPet.getId(), petId);
        assertEquals(Gender.MALE, pet.getGender());
	}

	/**
	 * Creates a pet with no user. Expects PetException with
	 * message "User does not exist: a pet needs a user".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoUser() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, "", PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: User does not exist.");
		}
        assertNull(pet);
	}

	/**
	 * Creates a pet with no name. Expects PetException with
	 * message "A pet needs a name".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoName() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try {
			PetDTO petDto = createPetDto(PET_DOB, "", PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a name.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no species. Expects PetException with
	 * message "A pet needs a species".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoSpecies() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a species.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no breed. Expects IllegalArgumentException with
	 * message "A pet needs a breed".
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoBreed() {
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME, PET_GENDER);
			pet = petService.createPet(petDto);
		} catch(PetException e) {
			assertEquals(e.getMessage(), "Cannot add: A pet needs a breed.");
		}
        assertNull(pet);
	}
	
	/**
	 * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
	 * @author Katrina
	 */
	@Test
	public void testEditPetNoDesc() {
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
		assertEquals(0, petService.getAllPets().size());
		pet = null;
		try{
			PetDTO petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME, null);
			pet = petService.createPet(petDto);
        } catch (PetException e) {
            assertEquals(e.getMessage(), "Cannot add: A pet needs a gender.");
        }
        assertNull(pet);
    }
    
    @Test
    public void testChangeOwnership() {
        //TODO
    }
    
    //why do you need this??
    private PetDTO createPetDto(Date petDob, String petName, String petSpecies, String petBreed, String petDescription,
                                byte[] petPicture, String userName, Gender petGender) {
        PetDTO dto = new PetDTO(petDob, petName, petSpecies, petBreed, petDescription, petPicture, petGender, null, userName);
        return dto;
    }
}
