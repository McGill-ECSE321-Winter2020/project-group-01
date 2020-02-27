package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestApplicationService {
	
    private static final String USER_NAME = "TestPerson";
    private static final String USER_EMAIL = "TestPerson@email.com";
    private static final String USER_PASSWORD = "myP1+abc";
	private static final String APPLICATION_DESCRIPTION = "I will watch Kim's Convinience and Trailer Park Boys with your pet every day if you accept my application. But, I hate JavaScript, so if you code in JavaScript, I am not interested in your pet.";
	private static final boolean IS_ACCEPTED = false;
	private static final String ADVERTISEMENT_DESCRIPTION = "My pet likes to watch Kim's Convinience and Trailer Park Boys.";
	private static final String ADVERTISEMENT_TITLE = "I HAVE THE BEST PET!";
	private static final boolean IS_FULFILLED = false;
	
	@Mock
	private UserRepository userRepository;
	private ApplicationRepository applicationRepository;
	private AdvertisementRepository advertisementRepository;
	
	@InjectMocks
	private UserService userService;
	private ApplicationService applicationService;
	
	@BeforeEach
	public void setMockOutput() {
		MockitoAnnotations.initMocks(this);
		lenient().when(userRepository.findUserByUserName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_NAME)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				return user;
			} else {
				return null;
			}
		});
		lenient().when(userRepository.findUserByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_EMAIL)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				return user;
			} else {
				return null;
			}
		});
		lenient().when(applicationRepository.findApplicationByUserUserNameAndAdvertisement(anyString(), advertisementRepository.findAdvertisementByTitle(anyString()))).thenAnswer((InvocationOnMock invocation) -> {
            
            if (invocation.getArgument(0).equals(USER_NAME)) {
                User user = new User();
                user.setUserName(USER_NAME);
                user.setEmail(USER_EMAIL);
                user.setPassword(USER_PASSWORD);
                Advertisement advertisement = new Advertisement();
                advertisement.setAdoptionApplication(null);
                advertisement.setDescription(ADVERTISEMENT_DESCRIPTION);
                advertisement.setTitle(ADVERTISEMENT_TITLE);
                advertisement.setIsFulfilled(IS_FULFILLED);
                if (invocation.getArgument(1).equals(APPLICATION_DESCRIPTION)) {
                    AdoptionApplication application = new AdoptionApplication();
                    application.setAdvertisement(advertisement);
                    application.setDescription(APPLICATION_DESCRIPTION);
                    application.setIsAccepted(IS_ACCEPTED);
                    application.setUser(user);
                    return application;
                }
            }
            return null;
        });
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
		lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(applicationRepository.save(any(AdoptionApplication.class))).thenAnswer(returnParameterAsAnswer);
	}

	@Test
    public void testCreateApplication() {
        assertEquals(0, applicationService.getAllApplications().size());
        ApplicationDTO applicationDTO = new ApplicationDTO();
        
        applicationDTO.setUsername(USER_NAME);
        applicationDTO.setAdvertisementTitle(ADVERTISEMENT_TITLE);
        applicationDTO.setDescription(APPLICATION_DESCRIPTION);
        applicationDTO.setIsAccepted(IS_ACCEPTED);
        
        try {
            applicationService.createApplication(applicationDTO);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        
        AdoptionApplication application = applicationRepository.findApplicationByUserUserNameAndAdvertisement(USER_NAME, advertisementRepository.findAdvertisementByTitle(ADVERTISEMENT_TITLE));
        assertEquals(USER_NAME, application.getUser().getUserName());
        assertEquals(ADVERTISEMENT_TITLE, application.getAdvertisement().getTitle());
        assertEquals(APPLICATION_DESCRIPTION, application.getDescription());
        assertEquals(IS_ACCEPTED, application.isIsAccepted());
    }
    
    //todo cant test this with mockito
    @Test
    public void testAnonymousDonation() {
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 11.22;
        
        donationDTO.setUser(null);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            e.printStackTrace();
        }
        
        List<Donation> allDonations = donationRepository.findAllByUser(null);
        System.out.println(allDonations);
        //  assertNull(allDonations.get(0).getUser());
    }
    
    @Test
    public void testNegativeDonation() {
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = -11.22;
        donationDTO.setUser(USER_NAME);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation amount can't be less than 0$", e.getMessage());
        }
    }
    
    @Test
    public void testZeroDonation() {
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 0;
        donationDTO.setUser(USER_NAME);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation amount can't be less than 0$", e.getMessage());
        }
    }
    
    @Test
    public void testMinimumDonation() {
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 0.01;
        donationDTO.setUser(USER_NAME);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation amount can't be less than 0$", e.getMessage());
        }
    }
    
    // no donation amount
    @Test
    public void testNullDonation() {
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        donationDTO.setUser(USER_NAME);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(null);
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation can't be null!", e.getMessage());
        }
    }
}