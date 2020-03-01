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
        //advertisementDTO.setPetIds(AD_PET_IDS);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("A pet must be linked to an advertisement.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateAdNoTitle() {
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
        //advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPetIds(AD_PET_IDS);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("An advertisement needs a title", e.getMessage());
        }
    }
    
    @Test
    public void testCreatePetNoSpecies() {
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
        //    advertisementDTO.setDescription(AD_DESCRIPTION);
        
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("An advertisement needs a description", e.getMessage());
        }
    }
    
    @Test
    public void testCreatePetAlreadyHaveAd() {
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
        advertisementDTO.setAdId(createdAd.getAdId());
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("One or more pets already have an advertisement.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateAdNoNotExist() {
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
        AD_PET_IDS[0] = 50L;
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPetIds(AD_PET_IDS);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("One or more pets do not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testEditPet() {
    
    }
}

