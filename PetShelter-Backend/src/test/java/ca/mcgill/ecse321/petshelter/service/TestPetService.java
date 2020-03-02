package ca.mcgill.ecse321.petshelter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.PetException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TestPetService {
	@InjectMocks
	private PetService petService;
	@InjectMocks
	private UserService userService;

	@Mock
	private AdvertisementRepository advertisementRepository;

	@Mock
	private AdvertisementService advertisementService;

	private static final Set<Pet> USER_PETS = new HashSet<>();

	// User parameters
	private static final String USER_NAME = "testUN";
	private static final String USER_EMAIL = "username@gmail.com";
	private static final String USER_PASSWORD = "testUN";
	private static final String USER_NAME2 = "testUN2";
	private static final String USER_TOKEN = "qwe";

	// Pet parameters
	private static final Date PET_DOB = new Date(119, 10, 20);
	private static final String PET_NAME = "testPettt";
	private static final String PET_SPECIES = "testSpecies";
	private static final String PET_BREED = "testBreed";
	private static final String PET_DESCRIPTION = "testDesc";
	private static final Gender PET_GENDER = Gender.FEMALE;
	private static final String PET_PICTURE = "hi";
	// Updated pet parameter
	private static final Date PET_DOB_UPDATE = new Date(11, 2, 3);
	private static final String PET_NAME_UPDATE = "newPetName";
	private static final String PET_SPECIES_UPDATE = "newTestSpecies";
	private static final String PET_BREED_UPDATE = "newTestBreed";
	private static final String PET_DESCRIPTION_UPDATE = "newTestDesc";
	private static final Gender PET_GENDER_UPDATE = Gender.MALE;
	private static final String PET_PICTURE_UPDATE = "hii";
	private long PET_ID = 0;
	private Pet PET = new Pet();

	private static final long AD_ID = 13;
	private static final long AD_ID2 = 15;

	@Mock
	private PetRepository petRepository;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void setMockOutput() {
		MockitoAnnotations.initMocks(this);
		lenient().when(userRepository.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_NAME)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				USER_PETS.add(PET);
				user.setPets(USER_PETS);
				return user;
			} else if (invocation.getArgument(0).equals(USER_NAME2)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				USER_PETS.add(PET);
				return user;
			} else {
				return null;
			}
		});

		lenient().when(userRepository.findUserByApiToken(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_TOKEN)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				USER_PETS.add(PET);
				user.setPets(USER_PETS);
				user.setApiToken(USER_TOKEN);
				return user;
			} else {
				return null;
			}
		});
		// this will never work because petid is null.....
		lenient().when(petRepository.findPetById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(PET_ID)) {
				PET.setBreed(PET_BREED);
				PET.setDateOfBirth(PET_DOB);
				PET.setDescription(PET_DESCRIPTION);
				PET.setName(PET_NAME);
				PET.setSpecies(PET_SPECIES);
				PET.setPicture(PET_PICTURE);
				PET.setGender(PET_GENDER);
				PET.setAdvertisement(new Advertisement());
				return PET;
			} else {
				return null;
			}
		});

		lenient().when(petRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			PET.setBreed(PET_BREED);
			PET.setDateOfBirth(PET_DOB);
			PET.setDescription(PET_DESCRIPTION);
			PET.setName(PET_NAME);
			PET.setSpecies(PET_SPECIES);
			PET.setPicture(PET_PICTURE);
			PET.setId(PET_ID);
			PET.setAdvertisement(new Advertisement());
			Set<Pet> pets = new HashSet<>();
			pets.add(PET);
			return pets;
		});

		lenient().when(petRepository.findPetByAdvertisement(any(Advertisement.class)))
				.thenAnswer((InvocationOnMock invocation) -> {
					if (((Advertisement) invocation.getArgument(0)).getId() == AD_ID) {
						PET.setBreed(PET_BREED);
						PET.setDateOfBirth(PET_DOB);
						PET.setDescription(PET_DESCRIPTION);
						PET.setName(PET_NAME);
						PET.setSpecies(PET_SPECIES);
						PET.setPicture(PET_PICTURE);
						PET.setId(PET_ID);
						PET.setAdvertisement(new Advertisement());
						List<Pet> pets = new ArrayList<>();
						pets.add(PET);
						return pets;
					} else if (((Advertisement) invocation.getArgument(0)).getId() == AD_ID2) {
						List<Pet> pets = new ArrayList<>();
						return pets;
					} else {
						return null;
					}
				});

		lenient().when(userRepository.findUserByPets(any())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0) instanceof Pet) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				user.setPets(USER_PETS);
				return user;
			} else {
				return null;
			}
		});
		lenient().when(advertisementRepository.findAdvertisementById(anyLong()))
				.thenAnswer((InvocationOnMock invocation) -> {
					if (invocation.getArgument(0).equals(AD_ID)) {
						Advertisement ad = new Advertisement();
						ad.setId(AD_ID);
						return ad;
					} else if (invocation.getArgument(0).equals(AD_ID2)) {
						Advertisement ad = new Advertisement();
						ad.setId(AD_ID2);
						return ad;
					} else {
						return null;
					}
				});

		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(petRepository.save(any(Pet.class))).thenAnswer(returnParameterAsAnswer);

	}

	/**
	 * Creates a test user in the database.
	 * 
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
		userRepository.save(user);

		return user;
	}

	//////////////////////////// GETTER METHODS ////////////////////////////

	/**
	 * Test fetching a pet by its ID.
	 */
	@Test
	public void testGetPet() {
		try {
			PetDTO petDTO = petService.getPet(PET_ID);
			assertEquals(PET_ID, petDTO.getId().longValue());
			assertEquals(PET_BREED, petDTO.getBreed());
			assertEquals(PET_DESCRIPTION, petDTO.getDescription());
			assertEquals(PET_DOB, petDTO.getDateOfBirth());
			assertEquals(PET_GENDER, petDTO.getGender());
			assertEquals(PET_SPECIES, petDTO.getSpecies());
			assertEquals(PET_PICTURE, petDTO.getPicture());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test fetching a pet that doesn't exist.
	 */
	@Test
	public void testGetMissingPet() {
		PetException thrown = assertThrows(PetException.class, () -> petService.getPet(344));
		assertTrue(thrown.getMessage().contains("Pet does not exist."));
	}

	/**
	 * Test fetching all pets.
	 */
	@Test
	public void testGetAllPets() {
		try {
			Set<PetDTO> pets = petService.getAllPets();
			assertTrue(pets.stream().map(PetDTO::getId).collect(Collectors.toSet()).contains(PET_ID));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test fetching all the pets of an ad.
	 */
	@Test
	public void getPetByAd() {
		try {
			List<PetDTO> pets = petService.getPetsByAdvertisement(AD_ID);
			assertTrue(pets.stream().map(PetDTO::getId).collect(Collectors.toSet()).contains(PET_ID));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test fetching pets of an ad that doesn't exist.
	 */
	@Test
	public void getPetByMissingAd() {
		PetException thrown = assertThrows(PetException.class, () -> petService.getPetsByAdvertisement(0));
		assertTrue(thrown.getMessage().contains("Advertisement does not exist."));
	}

	/**
	 * Test fetching pets of an ad that doesn't exist.
	 */
	@Test
	public void getMissingPetByAd() {
		try {
			List<PetDTO> pets = petService.getPetsByAdvertisement(AD_ID2);
			assertTrue(pets.isEmpty());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	////////////////////////////// CREATE PET //////////////////////////////

	/**
	 * Normal test case. Creates a pet. Should not throw any exception".
	 */
	@Test
	public void testCreatePet() {

		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO pet = null;

		try {
			pet = petService.createPet(petDTO);
		} catch (PetException e) {
			e.printStackTrace();
		}

		assertNotNull(pet);
		assertEquals(PET_DOB, pet.getDateOfBirth());
		assertEquals(PET_NAME, pet.getName());
	}

	@Test
	public void testCreatePetNoUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: User does not exist.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetEmptyUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName("");

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: User does not exist.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetNoName() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setAdvertisement(new Long(0));

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a name.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetEmptyName() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setName("");
		petDTO.setAdvertisement(new Long(0));

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a name.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetNoSpecies() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setUserName(USER_NAME);
		petDTO.setAdvertisement(new Long(0));

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a species.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetNoBreed() {
		PetDTO petDTO = new PetDTO();
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a breed.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetEmptyBreed() {
		PetDTO petDTO = new PetDTO();
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setBreed("");

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a breed.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetNoDesc() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO pet = null;
		try {
			pet = petService.createPet(petDTO);
		} catch (PetException e) {
			e.printStackTrace();
		}
		assertNotNull(pet);
		assertEquals("", pet.getDescription());
	}

	@Test
	public void testCreatePetNoGender() {

		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a gender.", e.getMessage());
		}
	}

	@Test
	public void testCreatePetEmptySpecies() {

		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setUserName(USER_NAME);
		petDTO.setSpecies("");

		try {
			petService.createPet(petDTO);
		} catch (PetException e) {
			assertEquals("Cannot add: A pet needs a species.", e.getMessage());
		}
	}

	@Test
	public void testEditPet() {
		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		PetDTO pet = petService.editPet(petUpdateDTO);

		assertNotNull(pet);
		assertEquals(PET_NAME_UPDATE, pet.getName());
		assertEquals(PET_BREED_UPDATE, pet.getBreed());
	}

	@Test
	public void testEditPetNoSpecies() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setAdvertisement(new Long(0));

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		// petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a species.", e.getMessage());
		}
	}

	@Test
	public void testEditPetNoBreed() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		// petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a breed.", e.getMessage());
		}
	}

	@Test
	public void testEditPetEmptySpecies() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setAdvertisement(new Long(0));

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());
		petUpdateDTO.setSpecies("");

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a species.", e.getMessage());
		}
	}

	@Test
	public void testEditPetEmptyBreed() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		// petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());
		petUpdateDTO.setBreed("");

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a breed.", e.getMessage());
		}
	}

	@Test
	public void testEditPetNoDesc() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		// petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public void testEditPetNoGender() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		// petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a gender.", e.getMessage());
		}
	}

	@Test
	public void testEditPetNoNewOwner() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		// petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: User not found.", e.getMessage());
		}
	}

	@Test
	public void testEditPetNoNewName() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		// petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a name.", e.getMessage());
		}
	}

	@Test
	public void testEditPetEmptyName() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName("");
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: A pet needs a name.", e.getMessage());
		}
	}

	@Test
	public void testEditPetWrongPet() {

		// creates pet
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setId(new Long(0));

		PetDTO oldPet = petService.createPet(petDTO);

		assertEquals(PET_NAME, oldPet.getName());

		// update the pet
		PetDTO petUpdateDTO = new PetDTO();
		petUpdateDTO.setBreed(PET_BREED_UPDATE);
		petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
		petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
		petUpdateDTO.setGender(PET_GENDER_UPDATE);
		petUpdateDTO.setName(PET_NAME_UPDATE);
		petUpdateDTO.setPicture(PET_PICTURE_UPDATE);
		petUpdateDTO.setSpecies(PET_SPECIES_UPDATE);
		petUpdateDTO.setUserName(USER_NAME);
		// petUpdateDTO.setId(oldPet.getId());

		try {
			petService.editPet(petUpdateDTO);
		} catch (PetException e) {
			assertEquals("Cannot edit: Pet does not exist.", e.getMessage());
		}
	}

	@Test
	public void testdeletePet() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		PetDTO pet = null;
		try {
			pet = petService.createPet(petDTO);
		} catch (PetException e) {
		}
		assertNotNull(pet);
		petDTO.setId(pet.getId());
		boolean isDeleted = false;
		try {
			isDeleted = petService.deletePet(petDTO.getId(), USER_NAME);
		} catch (PetException e) {
		}
		assertTrue(isDeleted);
	}

	@Test
	public void testdeletePetNoUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setId(new Long(0));
		try {
			petService.deletePet(petDTO.getId(), null);
		} catch (PetException e) {
			assertEquals("Cannot delete: User does not exist.", e.getMessage());
		}
	}

	@Test
	public void testdeletePetWrongUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName("WrongUser");
		petDTO.setId(new Long(0));
		try {
			petService.deletePet(petDTO.getId(), USER_NAME2);
		} catch (PetException e) {
			assertEquals("Cannot delete: The requester is not the owner of the pet.", e.getMessage());
		}

	}

	@Test
	public void testdeletePetNoId() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setId(new Long(1));
		try {
			petService.deletePet(petDTO.getId(), USER_NAME);
		} catch (PetException e) {
			assertEquals("Cannot delete: Pet does not exist.", e.getMessage());
		}
	}

	@Test
	public void testfindPet() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setAdvertisement(new Long(0));

		PetDTO pet = null;
		try {
			pet = petService.createPet(petDTO);
		} catch (PetException e) {
			e.printStackTrace();
		}

		assertNotNull(pet);
		petDTO.setId(pet.getId());

		PetDTO dbPet = petService.getPet(petDTO);
		assertEquals(pet.getName(), dbPet.getName());
		assertEquals(pet.getBreed(), dbPet.getBreed());
	}

	@Test
	public void testfindPetNoID() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);

		try {
			petService.getPet(petDTO);
		} catch (PetException e) {
			assertEquals("Pet does not exist.", e.getMessage());
		}
	}

	@Test
	public void testGetPetsByWrongUserName() {
		try {
			petService.getPetsByUser("bob");
		} catch (PetException e) {
			assertEquals("User does not exist.", e.getMessage());
		}
	}

	@Test
	public void testChangePetOwnership() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setId(PET_ID);
		petDTO = petService.changeOwner(petDTO, USER_TOKEN);
		assertEquals(petDTO.getDescription(), PET_DESCRIPTION);
	}

	@Test
	public void testChangePetOwnershipNoOldUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(USER_NAME);
		petDTO.setId(PET_ID);
		try {
			petDTO = petService.changeOwner(petDTO, null);
		} catch (PetException e) {
			assertEquals(e.getMessage(), "Cannot edit: User not found.");
		}

	}

	@Test
	public void testChangePetOwnershipNoNewUser() {
		PetDTO petDTO = new PetDTO();
		petDTO.setBreed(PET_BREED);
		petDTO.setDateOfBirth(PET_DOB);
		petDTO.setDescription(PET_DESCRIPTION);
		petDTO.setGender(PET_GENDER);
		petDTO.setName(PET_NAME);
		petDTO.setPicture(PET_PICTURE);
		petDTO.setSpecies(PET_SPECIES);
		petDTO.setUserName(null);
		petDTO.setId(PET_ID);
		try {
			petDTO = petService.changeOwner(petDTO, USER_TOKEN);
		} catch (PetException e) {
			assertEquals(e.getMessage(), "Cannot edit: User not found.");
		}

	}
}
