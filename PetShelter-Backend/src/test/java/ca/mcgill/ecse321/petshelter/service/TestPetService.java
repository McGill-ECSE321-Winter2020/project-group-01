package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.PetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

//TODO Javadoc
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TestPetService {
    
    
    private static final Set<Pet> USER_PETS = new HashSet<>();
    
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
    private static final Gender PET_GENDER = Gender.FEMALE;
    private static final byte[] PET_PICTURE = new byte[5];
    //Updated pet parameter
    private static final Date PET_DOB_UPDATE = new Date(11, 2, 3);
    private static final String PET_NAME_UPDATE = "newPetName";
    private static final String PET_SPECIES_UPDATE = "newTestSpecies";
    private static final String PET_BREED_UPDATE = "newTestBreed";
    private static final String PET_DESCRIPTION_UPDATE = "newTestDesc";
    private static final Gender PET_GENDER_UPDATE = Gender.MALE;
    private static final byte[] PET_PICTURE_UPDATE = new byte[10];
    private long PET_ID = 0;
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
                user.setPets(USER_PETS);
                return user;
            } else {
                return null;
            }
        });
        
        //this will never work because petid is null.....
        lenient().when(petRepository.findPetById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PET_ID)) {
                Pet pet = new Pet();
                pet.setBreed(PET_BREED);
                pet.setDateOfBirth(PET_DOB);
                pet.setDescription(PET_DESCRIPTION);
                pet.setName(PET_NAME);
                pet.setSpecies(PET_SPECIES);
                pet.setPicture(PET_PICTURE);
                return pet;
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
        
        Pet pet = null;
        
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
    public void testCreatePetNoName() {
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setGender(PET_GENDER);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
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
    public void testCreatePetNoDesc() {
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setGender(PET_GENDER);
        petDTO.setName(PET_NAME);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
        Pet pet = null;
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
    public void testEditPet() {
        //creates pet
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setGender(PET_GENDER);
        petDTO.setName(PET_NAME);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
        Pet oldPet = petService.createPet(petDTO);
        
        assertEquals(PET_NAME, oldPet.getName());
        
        //update the pet
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
        
        Pet pet = petService.editPet(petUpdateDTO);
        
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
        
        Pet oldPet = petService.createPet(petDTO);
        
        assertEquals(PET_NAME, oldPet.getName());
        
        //update the pet
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
        
        //creates pet
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setGender(PET_GENDER);
        petDTO.setName(PET_NAME);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
        Pet oldPet = petService.createPet(petDTO);
        
        assertEquals(PET_NAME, oldPet.getName());
        
        //update the pet
        PetDTO petUpdateDTO = new PetDTO();
        //     petUpdateDTO.setBreed(PET_BREED_UPDATE);
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
    public void testEditPetNoDesc() {
        
        //creates pet
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setGender(PET_GENDER);
        petDTO.setName(PET_NAME);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
        Pet oldPet = petService.createPet(petDTO);
        
        assertEquals(PET_NAME, oldPet.getName());
        
        //update the pet
        PetDTO petUpdateDTO = new PetDTO();
        petUpdateDTO.setBreed(PET_BREED_UPDATE);
        petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
        //   petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
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
        
        //creates pet
        PetDTO petDTO = new PetDTO();
        petDTO.setBreed(PET_BREED);
        petDTO.setDateOfBirth(PET_DOB);
        petDTO.setDescription(PET_DESCRIPTION);
        petDTO.setGender(PET_GENDER);
        petDTO.setName(PET_NAME);
        petDTO.setPicture(PET_PICTURE);
        petDTO.setSpecies(PET_SPECIES);
        petDTO.setUserName(USER_NAME);
        
        Pet oldPet = petService.createPet(petDTO);
        
        assertEquals(PET_NAME, oldPet.getName());
        
        //update the pet
        PetDTO petUpdateDTO = new PetDTO();
        petUpdateDTO.setBreed(PET_BREED_UPDATE);
        petUpdateDTO.setDateOfBirth(PET_DOB_UPDATE);
        petUpdateDTO.setDescription(PET_DESCRIPTION_UPDATE);
        //  petUpdateDTO.setGender(PET_GENDER_UPDATE);
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
    
}
