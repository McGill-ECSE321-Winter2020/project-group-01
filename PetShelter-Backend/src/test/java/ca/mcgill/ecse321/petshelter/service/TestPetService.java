package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PetDTO;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.PetRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

//TODO Javadoc
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TestPetService {
    
    private static final byte[] PET_PICTURE = new byte[5];
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
    @Mock
    private PetRepository petRepository;
    private static final Gender PET_GENDER = Gender.FEMALE;
    @Mock
    private UserRepository userRepository;
    private long petId;
    
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
        
        lenient().when(petRepository.findPetById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(petId)) {
                Pet pet = new Pet();
                pet.setBreed(PET_BREED);
                pet.setDateOfBirth(PET_DOB);
                pet.setDescription(PET_DESCRIPTION);
                pet.setName(PET_NAME);
                pet.setSpecies(PET_SPECIES);
                pet.setPicture(PET_PICTURE);
            } else {
                return null;
            }
            return null;
        });
        
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(petRepository.save(any(Pet.class))).thenAnswer(returnParameterAsAnswer);
        
    }
	

	/**
	 * Normal test case. Creates a pet. Should not throw any exception".
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
        
        Pet pet = petService.createPet(petDTO);
        
    }
    
}
