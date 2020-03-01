package ca.mcgill.ecse321.petshelter.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.MockitoJUnitRunner;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyString;

import java.sql.Date;
import java.util.ArrayList;
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

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.AdvertisementException;

//TODO Javadoc
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("deprecation")
public class TestAdvertisementService {

    @Mock
    private PetRepository petDao;

    @Mock
    private UserRepository userDao;
    
    @Mock
    private AdvertisementRepository adDao;

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
    private static final Set<Pet> USER1_PETS = new HashSet<Pet>();
    private static final Set<Pet> USER2_PETS = new HashSet<Pet>();
    private long petId;
    private long adId;
    private Pet pet;
    private AdvertisementDTO ad;
    private User user1;
    private User user2;
    private PetDTO petDto;
    private AdvertisementDTO adDto;



    //Ad parameters
    private static final String AD_TITLE = "testTitle";
    private static final String AD_DESCRIPTION = "testDescription";
    private static final boolean AD_FULLFILLED = false;
    private static List<Long> AD_PET_IDS = new ArrayList<Long>();
    private static final Set<Application> AD_APPLICATIONS = new HashSet<Application>();


    
    //TODO this is not called
    @AfterEach
    @BeforeEach
    public void clearDatabase() {
        userDao.deleteAll();
        petDao.deleteAll();
    }

    @Before
    public void setMockOutput() {
        //ad Id mock
        lenient().when(adDao.findAdvertisementById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(adId)) {
                ad = new AdvertisementDTO();
               // ad.setAdoptionApplication(AD_APPLICATIONS);
                ad.setDescription(AD_DESCRIPTION);
                ad.setFulfilled(AD_FULLFILLED);
                ad.setTitle(AD_TITLE);
                ad.setAdId(adId);
                return ad;
               } else {
                return null;
            }
        });
        
        lenient().when(userDao.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(USER_NAME1)) {
                user1 = new User();
                user1.setUserName(USER_NAME1);
                user1.setEmail(USER_EMAIL1);
                user1.setPassword(USER_PASSWORD1);
                user1.setUserType(USER_TYPE1);
                user1.setPets(USER1_PETS);

                return user1;

            } else if(invocation.getArgument(0).equals(USER_NAME2)) {
                user2 = new User();
                user2.setUserName(USER_NAME2);
                user2.setEmail(USER_EMAIL2);
                user2.setPassword(USER_PASSWORD2);
                user2.setUserType(USER_TYPE2);
                user2.setPets(USER2_PETS);

                return user2;

            } else {
                return null;
            }
        });


        lenient().when(userDao.findUserByPets(any(Pet.class))).thenAnswer((InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(pet)) {
                if(petDto.getUserName().equals(USER_NAME1)) {
                    return user1;
                } else if (petDto.getUserName().equals(USER_NAME2)){
                    return user2;
                } else {
                    return null;
                } 
            } else {
                return null;
            }
        });

        lenient().when(petDao.findPetById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(petId)) {
                pet.setAdvertisement(advertisementService.getAdvertisementById(ad.getAdId()));
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


    ////////////////////////////// CREATE AD //////////////////////////////

    /**
     * Normal test case. Creates a pet. Should not throw any exception".
     * @author Katrina
     */
    @Test
    public void testCreateAd() {
        assertEquals(0, advertisementService.getAllAdvertisements().size());
        pet = null;
        petDto = null;
        ad = null;
        adDto = null;
        user1 = userDao.findUserByUserName(USER_NAME1);
        petDto = createPetDto(PET_DOB, PET_NAME, PET_SPECIES, PET_BREED, PET_DESCRIPTION, PET_PICTURE, USER_NAME1, PET_GENDER);
        petDto = petService.createPet(petDto);
        assertNotNull(petDto);
        AD_PET_IDS.add(petDto.getId());
        adDto = createAdDto(AD_TITLE, AD_FULLFILLED, AD_PET_IDS, AD_APPLICATIONS, AD_DESCRIPTION);
        adDto = advertisementService.createAdvertisement(adDto);
        adId = adDto.getAdId();
        assertNotNull(ad);
        assertNotNull(adDao.findAdvertisementById(adId));
        assertEquals(AD_TITLE ,ad.getTitle());
        assertEquals(AD_DESCRIPTION, ad.getDescription());
        assertEquals(AD_FULLFILLED, ad.isFulfilled());
        assertEquals(AD_APPLICATIONS, ad.getApplication());

        for(Long id : AD_PET_IDS) {
            Pet pet = petDao.findPetById(id);
            assertTrue(pet.getAdvertisement().equals(ad));
        }
    }

    /**
     * Creates a pet with no user. Expects AdvertisementException with
     * message "User does not exist: a pet needs a user".
     * @author Katrina
     */
    @Test
    public void testCreateAdNoPet() {
        clearDatabase();
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
     */
    @Test
    public void testCreateAdNoTitle() {
        clearDatabase();
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
    
    private PetDTO createPetDto(Date petDob, String petName, String petSpecies, String petBreed, String petDescription,
            byte[] petPicture, String userName, Gender petGender) {
        PetDTO dto = new PetDTO(petDob, petName, petSpecies, petBreed, petDescription, petPicture, petGender, null, userName);
        return dto;
    }
    
    private AdvertisementDTO createAdDto (String title, boolean isfulfilled, List<Long> aD_PET_IDS, Set<Application> application, String description) {
        AdvertisementDTO dto = new AdvertisementDTO();
        
        dto.setTitle(title);
        dto.setFulfilled(isfulfilled);
        dto.setPetIds((Long[]) aD_PET_IDS.toArray());
        dto.setApplication(application);
        dto.setDescription(description);
        return dto;
    }
}

