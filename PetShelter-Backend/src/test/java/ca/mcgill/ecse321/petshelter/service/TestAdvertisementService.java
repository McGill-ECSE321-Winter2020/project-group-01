package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TestAdvertisementService {
    
    private static final Set<Pet> USER1_PETS = new HashSet<>();
    private static final Set<Pet> USER2_PETS = new HashSet<>();
    private static final Set<ApplicationDTO> AD_APPLICATIONS = new HashSet<>();
    
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
    private static final String PET_PICTURE = "picturelink";
    private static final Gender PET_GENDER = Gender.FEMALE;

    @Mock
    private PetRepository petRepository;
    @Mock
    private PetService petService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertisementRepository advertisementRepository;
    @Mock
    private ApplicationService applicationService;

    private final long PET_ID = 0;
    private final long AD_ID = 0;
    private final Pet PET = new Pet();
    private PetDTO PET_DTO = new PetDTO();

    //Ad parameters
    private static final String AD_TITLE = "testTitle";
    private static final String AD_DESCRIPTION = "testDescription";
    private static final boolean AD_FULLFILLED = false;
    private User USER1 = new User();
    private final User USER2 = new User();
    
    //Edited ad parameters
    private static final String AD_TITLE_EDIT = "newTestTitle";
    private static final String AD_DESCRIPTION_EDIT = "newTestDescription";
    private static final boolean AD_FULLFILLED_EDTI = true;
    
    @BeforeEach
    public void setMockOutput() {
        //ad Id mock
        lenient().when(advertisementRepository.findAdvertisementById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            
            if (invocation.getArgument(0).equals(AD_ID)) {

                Pet pet = new Pet();
                pet.setId(PET_ID);
                Advertisement ad = new Advertisement();
                ad.setDescription(AD_DESCRIPTION);
                ad.setIsFulfilled(AD_FULLFILLED);
                ad.setTitle(AD_TITLE);
                ad.setId(AD_ID);
                ad.setPet(pet);
                ad.setApplication(new HashSet<>());
                return ad;
            } else {
                return null;
            }
        });
        
        lenient().when(advertisementRepository.findAdvertisementByTitle(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            List<Advertisement> advertisementList = new ArrayList<>();
            HashSet<Application> applicationList = new HashSet<>();
            Advertisement ad = new Advertisement();

            Pet pet = new Pet();
            pet.setId(PET_ID);
            ad.setDescription(AD_DESCRIPTION);
            ad.setIsFulfilled(AD_FULLFILLED);
            ad.setTitle(AD_TITLE);
            ad.setId(AD_ID);
            ad.setPet(pet);
            ad.setApplication(applicationList);
            ad.setApplication(new HashSet<>());
            advertisementList.add(ad);
            return advertisementList;
        });
        
        lenient().when(advertisementRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Advertisement> advertisementList = new ArrayList<>();
            HashSet<Application> applicationList = new HashSet<>();
            Advertisement ad = new Advertisement();

            Pet pet = new Pet();
            pet.setId(PET_ID);
            ad.setDescription(AD_DESCRIPTION);
            ad.setIsFulfilled(AD_FULLFILLED);
            ad.setTitle(AD_TITLE);
            ad.setId(AD_ID);
            ad.setPet(pet);
            ad.setApplication(applicationList);
            advertisementList.add(ad);
            return advertisementList;
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
        
        lenient().when(petRepository.findPetById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (((long) invocation.getArgument(0)) == PET_ID) {
                PET.setDateOfBirth(PET_DOB);
                PET.setName(PET_NAME);
                PET.setSpecies(PET_SPECIES);
                PET.setDescription(PET_DESCRIPTION);
                PET.setPicture(PET_PICTURE);
                PET.setGender(PET_GENDER);
                PET.setId(PET_ID);
                Advertisement advertisement = new Advertisement();
                advertisement.setDescription(AD_DESCRIPTION);
                advertisement.setTitle(AD_TITLE);
                advertisement.setApplication(new HashSet<>());
                PET.setAdvertisement(advertisement);
                return PET;
            } else {
                return null;
            }
        });
        
        lenient().when(petRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            Set<Pet> petSet = new HashSet<>();
            petSet.add(PET);
            return petSet;
        });

        lenient().when(petService.convertToDTO(any(Pet.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0) != null) {
                Pet pet = invocation.getArgument(0);
                PetDTO petDTO = new PetDTO();
                petDTO.setId(pet.getId());
                petDTO.setDateOfBirth(pet.getDateOfBirth());
                petDTO.setSpecies(pet.getSpecies());
                petDTO.setPicture(pet.getPicture());
                petDTO.setName(pet.getName());
                petDTO.setGender(pet.getGender());
                petDTO.setDescription(pet.getDescription());
                petDTO.setBreed(pet.getBreed());
                if (pet.getAdvertisement() != null)
                    petDTO.setAdvertisement(pet.getAdvertisement().getId());
                return petDTO;
            } else {
                return null;
            }

        });

        lenient().when(applicationService.convertToDto(any(Application.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0) != null) {
                Application application = invocation.getArgument(0);
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setDescription(application.getDescription());
                applicationDTO.setUsername(application.getUser().getUserName());
                applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
                applicationDTO.setIsAccepted(application.isIsAccepted());
                applicationDTO.setAdId(application.getAdvertisement().getId());
                applicationDTO.setAppId(application.getId());
                return applicationDTO;
            } else {
                return null;
            }
        });
        
        
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(petRepository.save(any(Pet.class))).thenAnswer(returnParameterAsAnswer);
        
    }
    
    /**
     * Normal test case. Creates a pet. Should not throw any exception".
     *
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
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
        petDTO.setId(PET_ID);
    
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("An advertisement needs a title.", e.getMessage());
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        
        try {
            advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("An advertisement needs a description.", e.getMessage());
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
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
        petDTO.setId(50L);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
    
        AdvertisementDTO createdAd = advertisementService.createAdvertisement(advertisementDTO);
        assertNotNull(createdAd);
    
        AdvertisementDTO editAdvertisementDTO = new AdvertisementDTO();
        editAdvertisementDTO.setTitle(AD_TITLE_EDIT);
        editAdvertisementDTO.setFulfilled(AD_FULLFILLED_EDTI);
        editAdvertisementDTO.setPet(petDTO);
        editAdvertisementDTO.setApplication(AD_APPLICATIONS);
        editAdvertisementDTO.setDescription(AD_DESCRIPTION_EDIT);
        editAdvertisementDTO.setAdId(AD_ID);
    
        AdvertisementDTO editedAd = advertisementService.editAdvertisement(editAdvertisementDTO);
    
        assertNotNull(editedAd);
        assertNotEquals(createdAd.getTitle(), editedAd.getTitle());
        assertEquals(createdAd.getAdId(), editedAd.getAdId());
        assertEquals(AD_TITLE_EDIT, editedAd.getTitle());
    }
    
    @Test
    public void testEditAdNoID() {
        
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = advertisementService.createAdvertisement(advertisementDTO);
        assertNotNull(createdAd);
        
        AdvertisementDTO editAdvertisementDTO = new AdvertisementDTO();
        editAdvertisementDTO.setTitle(AD_TITLE_EDIT);
        editAdvertisementDTO.setFulfilled(AD_FULLFILLED_EDTI);
        editAdvertisementDTO.setPet(petDTO);
        editAdvertisementDTO.setApplication(AD_APPLICATIONS);
        editAdvertisementDTO.setDescription(AD_DESCRIPTION_EDIT);
        editAdvertisementDTO.setAdId(null);
        
        try {
            advertisementService.editAdvertisement(editAdvertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testEditAdNoTitle() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = advertisementService.createAdvertisement(advertisementDTO);
        assertNotNull(createdAd);
        
        AdvertisementDTO editAdvertisementDTO = new AdvertisementDTO();
        editAdvertisementDTO.setFulfilled(AD_FULLFILLED_EDTI);
        editAdvertisementDTO.setPet(petDTO);
        editAdvertisementDTO.setApplication(AD_APPLICATIONS);
        editAdvertisementDTO.setDescription(AD_DESCRIPTION_EDIT);
        editAdvertisementDTO.setAdId(createdAd.getAdId());
        
        try {
            advertisementService.editAdvertisement(editAdvertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("Title must not be null or empty.", e.getMessage());
        }
    }
    
    @Test
    public void testEditAdNoDescription() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);

        AdvertisementDTO createdAd = advertisementService.createAdvertisement(advertisementDTO);
        assertNotNull(createdAd);
        
        AdvertisementDTO editAdvertisementDTO = new AdvertisementDTO();
        editAdvertisementDTO.setTitle(AD_TITLE_EDIT);
        editAdvertisementDTO.setFulfilled(AD_FULLFILLED_EDTI);
        editAdvertisementDTO.setPet(petDTO);
        editAdvertisementDTO.setApplication(AD_APPLICATIONS);
        editAdvertisementDTO.setDescription("");
        editAdvertisementDTO.setAdId(0L);
        
        try {
            advertisementService.editAdvertisement(editAdvertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("Description must not be null or empty.", e.getMessage());
        }
    }
    
    @Test
    public void testEditAdNoPet() {
        AdvertisementDTO editAdvertisementDTO = new AdvertisementDTO();
        editAdvertisementDTO.setTitle(AD_TITLE_EDIT);
        editAdvertisementDTO.setFulfilled(AD_FULLFILLED_EDTI);
        editAdvertisementDTO.setApplication(AD_APPLICATIONS);
        editAdvertisementDTO.setDescription(AD_DESCRIPTION_EDIT);
        editAdvertisementDTO.setAdId(AD_ID);

        try {
            advertisementService.editAdvertisement(editAdvertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("One or more pets do not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testDeleteAd() {
        
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }

        createdAd.setAdId(AD_ID);
        
        assertNotNull(createdAd);
        AdvertisementDTO deletedAd = advertisementService.deleteAdvertisement(createdAd);
        assertNotNull(deletedAd);
        assertEquals(AD_TITLE, deletedAd.getTitle());
    }
    
    @Test
    public void testDeleteWrongAd() {
        
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        assertNotNull(createdAd);
        createdAd.setAdId(11L);
        System.out.println(createdAd);
        try {
            advertisementService.deleteAdvertisement(createdAd);
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testDeleteNoAd() {
        
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        assertNotNull(createdAd);
        createdAd.setAdId(null);
        try {
            advertisementService.deleteAdvertisement(createdAd);
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testGetAdvertisement() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        assertNotNull(createdAd);
        AdvertisementDTO dbAd = null;
        try {
            dbAd = advertisementService.getAdvertisement(createdAd);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        assertNotNull(dbAd);
        assertEquals(createdAd.getTitle(), dbAd.getTitle());
        assertEquals(createdAd.getDescription(), dbAd.getDescription());
    }
    
    @Test
    public void testGetWrongAdvertisement() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        assertNotNull(createdAd);
        createdAd.setAdId(null);
        try {
            advertisementService.getAdvertisement(createdAd);
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testGetNullAdvertisement() {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
    
        try {
            advertisementService.getAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    @Test
    public void testGetAdvertisementNullId() {
        try {
            advertisementService.getAdvertisement(new AdvertisementDTO());
        } catch (AdvertisementException e) {
            assertEquals("Advertisement does not exist.", e.getMessage());
        }
    }
    
    
    @Test
    public void testGetAllAdvertisementByTitle() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        List<AdvertisementDTO> advertisementDTOList = advertisementService.getAdvertisementByTitle(AD_TITLE);
        assertNotNull(createdAd);
        assertEquals(createdAd.getTitle(), advertisementDTOList.get(0).getTitle());
    }
    
    @Test
    public void testGetAllAdvertisementNoTitle() {
        try {
            advertisementService.getAdvertisementByTitle(null);
        } catch (AdvertisementException e) {
            assertEquals("No advertisement found.", e.getMessage());
        }
    }
    
    @Test
    public void testGetFindAll() {
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
        petDTO.setId(PET_ID);

        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setTitle(AD_TITLE);
        advertisementDTO.setFulfilled(AD_FULLFILLED);
        advertisementDTO.setPet(petDTO);
        advertisementDTO.setApplication(AD_APPLICATIONS);
        advertisementDTO.setDescription(AD_DESCRIPTION);
        
        AdvertisementDTO createdAd = null;
        try {
            createdAd = advertisementService.createAdvertisement(advertisementDTO);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        
        List<AdvertisementDTO> advertisementDTOList = advertisementService.getAllAdvertisements();
        assertNotNull(createdAd);
        assertEquals(createdAd.getTitle(), advertisementDTOList.get(0).getTitle());
    }
    
    @Test
    public void testFindAdvertisementByPet() {
        AdvertisementDTO adWithPet = null;
        
        try {
            adWithPet = advertisementService.getAdvertisementByPet(PET_ID);
        } catch (AdvertisementException e) {
            e.printStackTrace();
        }
        assertNotNull(adWithPet);
        assertEquals(AD_TITLE, adWithPet.getTitle());
        assertEquals(AD_DESCRIPTION, adWithPet.getDescription());
    }
    
    @Test
    public void testFindAdvertisementByPetNoPet() {
        try {
            advertisementService.getAdvertisementByPet(22L);
        } catch (AdvertisementException e) {
            assertEquals("Pet does not exist.", e.getMessage());
        }
    }
}

