package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.DonationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestDonationService {
	
	@Mock
	private DonationRepository donationRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private DonationService donationService;
	
	@InjectMocks
	private UserService userService;
	
	private static final String USER_NAME = "TestPerson";
	private static final String USER_EMAIL = "TestPerson@email.com";
	private static final String USER_PASSWORD = "myP1+abc";
	
	private static final double DONATION_AMOUNT = 2.12;
	private static final Date DONATION_DATE = Date.valueOf("2020-01-22");
	private static final Time DONATION_TIME = Time.valueOf("11:22:00");

	
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
//
		//todo check this
		lenient().when(donationRepository.findDonationsByUserUserNameAndAmount(anyString(), anyDouble())).thenAnswer((InvocationOnMock invocation) -> {
			
			if (invocation.getArgument(0).equals(USER_NAME)) {
				User user = new User();
				user.setUserName(USER_NAME);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASSWORD);
				if (invocation.getArgument(1).equals(DONATION_AMOUNT)) {
					Donation donation = new Donation();
					donation.setUser(user);
					donation.setTime(DONATION_TIME);
					donation.setDate(DONATION_DATE);
					donation.setAmount(DONATION_AMOUNT);
					return donation;
				}
			}
			return null;
		});
		
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
		lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(donationRepository.save(any(Donation.class))).thenAnswer(returnParameterAsAnswer);
	}

//	public User createUser() {
//		UserDTO userDTO = new UserDTO();
//		UserType userType = USER;
//
//		userDTO.setEmail(USER_EMAIL);
//		userDTO.setPassword(USER_PASSWORD);
//		userDTO.setUsername(USER_NAME);
//		userDTO.setUserType(userType);
//
//		try {
//			userService.createUser(userDTO);
//		} catch (RegisterException e) {
//			e.printStackTrace();
//		}
//		return userRepository.findUserByUserName(USER_NAME);
//	}
	
	// Regular use case
	@Test
	public void testDonation() {
		assertEquals(0, donationService.getAllDonations().size());
		DonationDTO donationDTO = new DonationDTO();
		
		donationDTO.setUser(USER_NAME);
		donationDTO.setAmount(DONATION_AMOUNT);
		donationDTO.setDate(DONATION_DATE);
		donationDTO.setTime(DONATION_TIME);
		
		try {
			donationService.createDonation(donationDTO);
		} catch (DonationException e) {
			e.printStackTrace();
		}
		
		Donation donation = donationRepository.findDonationsByUserUserNameAndAmount(USER_NAME, DONATION_AMOUNT);
		assertEquals(DONATION_AMOUNT, donation.getAmount());
//		List<Donation> allDonations = donationService.getAllDonations();
//		assertEquals(userName, allDonations.get(0).getUser().getUserName());
//		assertEquals(amount, allDonations.get(0).getAmount());
//		assertEquals(date, allDonations.get(0).getDate());
	}
/*
	// Anonymous donation
	@Test
	public void testAnonymousDonation() {
		DonationDTO donationDTO = new DonationDTO();
		String username = "bobby";
		Date date = Date.valueOf("2020-01-22");
		Time time = Time.valueOf("11:22:00");
		double amount = 11.22;

		donationDTO.setUser(username);
		donationDTO.setTime(time);
		donationDTO.setDate(date);
		donationDTO.setAmount(amount);

		try {
			donationService.createDonation(donationDTO);
		} catch (DonationException ignored) {

		}

		List<Donation> allDonations = donationService.getAllDonations();
		assertNull(allDonations.get(0).getUser());
	}

	// negative donation amount
	@Test
	public void testNegativeDonation() {
		createUser();
		DonationDTO donationDTO = new DonationDTO();
		Date date = Date.valueOf("2020-01-22");
		Time time = Time.valueOf("11:22:00");
		double amount = -11.22;

		donationDTO.setUser(name);
		donationDTO.setTime(time);
		donationDTO.setDate(date);
		donationDTO.setAmount(amount);

		try {
			donationService.createDonation(donationDTO);
		} catch (DonationException e) {
			assertEquals("Donation amount can't be less than 0$", e.getMessage());
		}
	}

	// zero donation amount, edge case
	@Test
	public void testZeroDonation() {
		createUser();
		DonationDTO donationDTO = new DonationDTO();
		Date date = Date.valueOf("2020-01-22");
		Time time = Time.valueOf("11:22:00");
		double amount = 0;

		donationDTO.setUser(name);
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
		createUser();
		DonationDTO donationDTO = new DonationDTO();
		Date date = Date.valueOf("2020-01-22");
		Time time = Time.valueOf("11:22:00");
		double amount = 0.01;

		donationDTO.setUser(name);
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
		createUser();
		DonationDTO donationDTO = new DonationDTO();
		Date date = Date.valueOf("2020-01-22");
		Time time = Time.valueOf("11:22:00");

		donationDTO.setUser(name);
		donationDTO.setTime(time);
		donationDTO.setDate(date);
		donationDTO.setAmount(null);
		try {
			donationService.createDonation(donationDTO);
		} catch (DonationException e) {
			assertEquals("Donation can't be null!", e.getMessage());
		}
	}
	*/
}
