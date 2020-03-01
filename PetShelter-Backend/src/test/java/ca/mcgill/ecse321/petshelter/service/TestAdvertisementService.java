package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.*;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.AdvertisementException;
import ca.mcgill.ecse321.petshelter.service.exception.PetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TestAdvertisementService {
    
    private static final Set<Pet> USER1_PETS = new HashSet<>();
    private static final Set<Pet> USER2_PETS = new HashSet<>();
    private static final Set<Application> AD_APPLICATIONS = new HashSet<>();
    
    @InjectMocks
    private UserService userService;
    
    @InjectMocks
    private PetService petService;
    
    @InjectMocks
    private AdvertisementService advertisementService;

    //User1 parameters
    private static final String USER_NAME1 = "testUN";
    private static final String USER_EMAIL1 = "username@gmail.com";
    private static final String USER_PASSWORD1 = "password1";
    private static final UserType USER_TYPE1 = UserType.USER;

    //User2 parameters
    private static final String USER_NAME2 = "secondUser";
    private static final String USER_EMAIL2 = "username2@gmail.com";
    private static final String USER_PASSWORD2 = "password2";
    private static final UserType USER_TYPE2 = UserType.USER;
    
    //Pet parameters
    private static final Date PET_DOB = new Date(119, 10, 20);
    private static final String PET_NAME = "testPettt";
    private static final String PET_SPECIES = "testSpecies";
    private static final String PET_BREED = "testBreed";
    private static final String PET_DESCRIPTION = "testDesc";
    private static final byte[] PET_PICTURE = new byte[10];
    private static final Gender PET_GENDER = Gender.FEMALE;
    private static Long[] AD_PET_IDS = new Long[1];
    @Mock
    private PetRepository petRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertisementRepository advertisementRepository;
    private long PET_ID = 0;
    private long AD_ID = 0;
    private Pet PET = new Pet();
    
    
    //Ad parameters
    private static final String AD_TITLE = "testTitle";
    private static final String AD_DESCRIPTION = "testDescription";
    private static final boolean AD_FULLFILLED = false;
    private User USER1 = new User();
    private User USER2 = new User();
    
    @BeforeEach
    public void setMockOutput() {
        //ad Id mock
        lenient().when(advertisementRepository.findAdvertisementById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(AD_ID)) {
                Advertisement ad = new Advertisement();
                // ad.setAdoptionApplication(AD_APPLICATIONS);
                ad.setDescription(AD_DESCRIPTION);
                ad.setIsFulfilled(AD_FULLFILLED);
                ad.setTitle(AD_TITLE);
                ad.setId(AD_ID);
                return ad;
            } else {
                return null;
            }
        });
        
        lenient().when(userRepository.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_NAME1)) {
                USER1.setUserName(USER_NAME1);
                USER1.setEmail(USER_EMAIL1);
                USER1.setPassword(USER_PASSWORD1);
                USER1.setUserType(USER_TYPE1);
                USER1.setPets(USER1_PETS);
                return USER1;
            } else if (invocation.getArgument(0).equals(USER_NAME2)) {
                USER2.setUserName(USER_NAME2);
                USER2.setEmail(USER_EMAIL2);
                USER2.setPassword(USER_PASSWORD2);
                USER2.setUserType(USER_TYPE2);
                USER2.setPets(USER2_PETS);
                return USER2;
            } else {
                return null;
            }
        });
        
        
        lenient().when(userRepository.findUserByPets(any(Pet.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PET)) {
                return USER1;
            } else if (invocation.getArgument(0).equals(USER_NAME2)) {
                return USER2;
            } else {
                return null;
            }
        });
        
        lenient().when(petRepository.findPetById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PET_ID)) {
                PET.setDateOfBirth(PET_DOB);
                PET.setName(PET_NAME);
                PET.setSpecies(PET_SPECIES);
                PET.setDescription(PET_DESCRIPTION);
                PET.setPicture(PET_PICTURE);
                PET.setGender(PET_GENDER);
                //   PET.setAdvertisement(advertisementService.getAdvertisementById(AD_ID));
                return PET;
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
     * Normal test case. Creates a pet. Should not throw any exception".
     * @author Katrina
     */
    @Test
    public void testCreateAd() {
        USER1 = userRepository.findUserByUserName(USER_NAME1);
        PetDTO petDTO = new PetDTO();
    
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setName(PET_NAME);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setBreed(PET_BREED);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setGender(PET_GENDER);
        petDTO.setUserName(USER_NAME1);
    
        PetDTO createdPet = null;
        try {
            createdPet = petService.createPet(petDTO);
        } catch (PetException e) {
            e.printStackTrace();
        }
        assertNotNull(createdPet);
        AD_PET_IDS[0] = createdPet.getId();
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPetIds(AD_PET_IDS);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
    
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        assertNotNull(createdAd);
        assertEquals(AD_TITLE, createdAd.getTitle());
        assertEquals(AD_DESCRIPTION, createdAd.getDescription());
    }
    
    @Test
    public void testCreateAdNoPet() {
    
    }
    /*

    /**
     * Creates a pet with no user. Expects AdvertisementException with
     * message "User does not exist: a pet needs a user".
     * @author Katrina
     
    @Test
    public void testCreateAdNoPet() {
        assertEquals(0, petService.getAllPets().size());
        AD_PET_IDS.clear();
        pet = null;
        petDto = null;
        ad = null;
        adDto = null;
        petDto = petService.createPet(createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER));
        try {
            adDto = createAdDto(AD_TITLE, AD_FULLFILLED, AD_PET_IDS, AD_APPLICATIONS, AD_DESCRIPTION);
            ad = advertisementService.createAdvertisement(adDto);
        } catch(AdvertisementException e) {
            assertEquals(e.getMessage(), "A pet must be linked to an advertisement.");
        }
        assertEquals(ad, null);
    }

    /**
     * Creates a pet with no name. Expects AdvertisementException with
     * message "A pet needs a name".
     * @author Katrina
     
    @Test
    public void testCreateAdNoTitle() {]
        assertEquals(0, petService.getAllPets().size());
        
        pet = null;
        petDto = null;
        ad = null;
        adDto = null;
        petDto = petService.createPet(createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER));

        AD_PET_IDS.add(pet.getId());
        try {
            adDto = createAdDto("", AD_FULLFILLED, AD_PET_IDS, AD_APPLICATIONS, AD_DESCRIPTION);
            ad = advertisementService.createAdvertisement(adDto);
        } catch(AdvertisementException e) {
            assertEquals(e.getMessage(), "An advertisement needs a title");
        }
        assertEquals(ad, null);
    }
*/
    
    //
//    /**
//     * Creates a pet with no species. Expects AdvertisementException with
//     * message "A pet needs a species".
//     * @author Katrina
//     */
//    @Test
//    public void testCreatePetNoSpecies() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        try{
//            petDto = createPetDto(PET_DOB, PET_NAME, "", PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            petDto = petService.createPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot add: A pet needs a species.");
//        }
//        assertEquals(pet, null);
//    }
//
//    /**
//     * Creates a pet with no breed. Expects IllegalArgumentException with
//     * message "A pet needs a breed".
//     * @author Katrina
//     */
//    @Test
//    public void testCreatePetNoBreed() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        try{
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, "", PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            petDto = petService.createPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot add: A pet needs a breed.");
//        }
//        assertEquals(pet, null);
//    }
//
//    /**
//     * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
//     * @author Katrina
//     */
//    @Test
//    public void testCreatePetNoDesc() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        petDto = petService.createPet(createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, "", PET_PICTURE, USER_NAME1, PET_GENDER));
//        assertNotNull(pet);
//        assertEquals("", pet.getDescription());
//        assertNotNull(petDao.findPetById(pet.getId()));
//    }
//
//    /**
//     * Creates a pet with no gender. Expects IllegalArgumentException with
//     * message "A pet needs a gender".
//     * @author Katrina
//     */
//    @Test
//    public void testCreatePetNoGender() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        try{
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, null);
//            petDto = petService.createPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot add: A pet needs a gender.");
//        }
//        assertEquals(petDto, null);
//    }

    ////////////////////////////// EDIT PET //////////////////////////////
    //TODO everything here
    /**
     * Normal test case. Creates a pet. Should not throw any exception".
     * @author Katrina
     */
//    @Test
//    public void testEditPet() {
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//        petDto = petService.createPet(petDto);
//        petId = pet.getId();
//        assertNotNull(petDto);
//        assertNotNull(petDao.findPetById(petId));
//        assertEquals("testPettt",pet.getName());
//        assertEquals(new Date(119, 10, 20), pet.getDateOfBirth());
//        assertEquals("testSpecies", pet.getSpecies());
//        assertEquals("testBreed", pet.getBreed());
//        assertEquals("testDesc", pet.getDescription());
//        assertEquals(PET_PICTURE, pet.getPicture());
//        User fetchedUser = userDao.findUserByUserName(USER_NAME1);
//        Pet fetchedPet = (Pet) fetchedUser.getPets().toArray()[0];
//        assertTrue(fetchedPet.getId() == petId);
//        assertEquals(Gender.FEMALE, pet.getGender());   
//
//        petDto.setName("newName");
//        petDto.setSpecies("newSpecies");
//        petDto.setBreed("newBreed");
//        petDto.setDescription("newDesc");
//        byte[] newPic = new byte[10];
//        petDto.setPicture(newPic);
//        petDto.setGender(Gender.MALE);
//        petDto = petService.editPet(petDto);
//        assertNotNull(petDto);
//        assertNotNull(petDao.findPetById(petId));
//        assertEquals("newName",pet.getName());
//        assertEquals(PET_DOB, pet.getDateOfBirth());
//        assertEquals("newSpecies", pet.getSpecies());
//        assertEquals("newBreed", pet.getBreed());
//        assertEquals("newDesc", pet.getDescription());
//        assertEquals(newPic, pet.getPicture());
//        fetchedUser = userDao.findUserByUserName(USER_NAME1);
//        fetchedPet = (Pet) fetchedUser.getPets().toArray()[0];
//        assertTrue(fetchedPet.getId() == petId);
//        assertEquals(Gender.MALE, pet.getGender()); 
//    }
//
//    /**
//     * Creates a pet with no user. Expects AdvertisementException with
//     * message "User does not exist: a pet needs a user".
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoUser() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        Pet oldPet = null;
//        try {
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            petDto = petService.createPet(petDto);
//            petDto.setUserName("");
//            petDto = petService.editPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: User not found.");
//        }
//        assertEquals(petDto, oldPet);
//    }
//
//    /**
//     * Creates a pet with no name. Expects AdvertisementException with
//     * message "A pet needs a name".
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoName() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        Pet oldPet = null;
//        petDto = null;
//        try {
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            pet = petService.createPet(petDto);
//            oldPet = pet;
//            petDto.setName("");
//            pet = petService.editPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: A pet needs a name.");
//        }
//        assertEquals(pet, oldPet);
//    }
//
//    /**
//     * Creates a pet with no species. Expects AdvertisementException with
//     * message "A pet needs a species".
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoSpecies() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        Pet oldPet = null;
//        try {
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            pet = petService.createPet(petDto);
//            oldPet = pet;
//            petDto.setSpecies("");
//            pet = petService.editPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: A pet needs a species.");
//        }
//        assertEquals(pet, oldPet);
//    }
//
//    /**
//     * Creates a pet with no breed. Expects IllegalArgumentException with
//     * message "A pet needs a breed".
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoBreed() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        Pet oldPet = null;
//        try {
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            pet = petService.createPet(petDto);
//            oldPet = pet;
//            petDto.setBreed("");
//            pet = petService.editPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: A pet needs a breed.");
//        }
//        assertEquals(pet, oldPet);
//    }
//
//    /**
//     * Creates a pet with no description. Should not throw an exception as empty and null descriptions are allowed.
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoDesc() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//        pet = petService.createPet(petDto);
//        petDto.setDescription("");
//        pet = petService.editPet(petDto);
//        assertEquals(pet, petDao.findPetById(pet.getId()));
//        assertEquals("testPettt",pet.getName());
//        assertEquals(new Date(119, 10, 20), pet.getDateOfBirth());
//        assertEquals("testSpecies", pet.getSpecies());
//        assertEquals("testBreed", pet.getBreed());
//        assertEquals("", pet.getDescription());
//        assertEquals(PET_PICTURE, pet.getPicture());
//        assertEquals(Gender.FEMALE, pet.getGender());
//    }
//
//    /**
//     * Creates a pet with no gender. Expects IllegalArgumentException with
//     * message "A pet needs a gender".
//     * @author Katrina
//     */
//    @Test
//    public void testEditPetNoGender() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        petDto = null;
//        Pet oldPet = null;
//        try {
//            petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//            pet = petService.createPet(petDto);
//            oldPet = pet;
//            petDto.setGender(null);
//            pet = petService.editPet(petDto);
//        } catch(AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: A pet needs a gender.");
//        }
//        assertEquals(pet, oldPet);
//    }
//
//    @Test
//    public void testChangeOwnership() {
//        //TODO
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        Pet oldPet = null;
//        petDto = null;
//        petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//        pet = petService.createPet(petDto);
//        assertTrue(user1.getPets().contains(pet));            
//
//        petDto.setUserName(USER_NAME2);
//        user2 = userDao.findUserByUserName(USER_NAME2);
//        pet = petService.editPet(petDto);
//        assertTrue(user2.getPets().contains(pet));
//        
//    }
//    @Test //new user is null
//    public void testChangeToNullUser() {
//        clearDatabase();
//        assertEquals(0, petService.getAllPets().size());
//        pet = null;
//        Pet oldPet = null;
//        petDto = null;
//        petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
//        pet = petService.createPet(petDto);
//        assertTrue(user1.getPets().contains(pet));            
//        try {
//        petDto.setUserName("");
//        pet = petService.editPet(petDto);
//        assertTrue(user2.getPets().contains(pet));
//        } catch (AdvertisementException e) {
//            assertEquals(e.getMessage(), "Cannot edit: User not found.");
//        }
//    }
}

