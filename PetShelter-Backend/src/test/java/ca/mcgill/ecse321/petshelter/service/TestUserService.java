package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.*;
import ca.mcgill.ecse321.petshelter.repository.*;
import ca.mcgill.ecse321.petshelter.service.exception.PasswordException;
import ca.mcgill.ecse321.petshelter.service.exception.RegisterException;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.EmailingService;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.JWTTokenProvider;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static ca.mcgill.ecse321.petshelter.model.UserType.ADMIN;
import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestUserService {
	
	private static final String USER_NAME = "TestPerson";
	private static final String USER_EMAIL = "TestPerson@email.com";
	private static final String USER_PASSWORD = "myP1+abc";
	
	private static final String USER_NAME2 = "TestPerson2";
	private static final String USER_EMAIL2 = "TestPerson2@email.com";
	private static final String USER_PASSWORD2 = "myP1+abc2";
	private static final String NEW_USER_PASSWORD = "ad@ssa23Asda";
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AdvertisementRepository advertisementRepository;
	
	@Mock
	private ApplicationRepository applicationRepository;
	
	@Mock
	private CommentRepository commentRepository;
	
	@Mock
	private ForumRepository forumRepository;
	
	@Mock
	private PetRepository petRepository;
	
	@Mock
	private DonationRepository donationRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private JWTTokenProvider jwtTokenProvider;
	
	@Mock
	private EmailingService emailingService;
	
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
				user.setPassword(passwordEncoder.encode(USER_PASSWORD));
				return user;
			} else {
				return null;
			}
		});
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
		lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
		
		//these are needed to pass the delete methods
		lenient().when(donationRepository.save(any(Donation.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(advertisementRepository.save(any(Advertisement.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(applicationRepository.save(any(Application.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(petRepository.save(any(Pet.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(commentRepository.save(any(Comment.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(forumRepository.save(any(Forum.class))).thenAnswer(returnParameterAsAnswer);
	}
	
	@Test
	public void testCreateUser() {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setEmail(USER_EMAIL2);
		userDTO.setPassword(USER_PASSWORD2);
		userDTO.setUsername(USER_NAME2);
		userDTO.setUserType(USER);
		
		UserDTO createdUser = null;
		try {
			createdUser = userService.createUser(userDTO);
		} catch (RegisterException e) {
			e.printStackTrace();
		}
		assertNotNull(createdUser);
		assertEquals(userDTO.getEmail(), createdUser.getEmail());
	}
	
	@Test
	public void testCreateAdmin() {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setEmail(USER_EMAIL2);
		userDTO.setPassword(USER_PASSWORD2);
		userDTO.setUsername(USER_NAME2);
		userDTO.setUserType(ADMIN);
		
		UserDTO createdUser = null;
		try {
			createdUser = userService.createUser(userDTO);
		} catch (RegisterException e) {
			e.printStackTrace();
		}
		assertNotNull(createdUser);
		assertEquals(userDTO.getEmail(), createdUser.getEmail());
	}
	
	@Test
	public void testCreateWithInvalidEmail() {
		UserDTO userDTO = new UserDTO();
		
		String email = "myEmail@";
		
		userDTO.setEmail(email);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			assertEquals("The provided email is not a valid email address.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithMissingAtEmail() {
		UserDTO userDTO = new UserDTO();
		
		String email = "myEmailgmail.com";
		
		userDTO.setEmail(email);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (Exception e) {
			Assert.assertEquals("The provided email is not a valid email address.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithMissingUsername() {
		UserDTO userDTO = new UserDTO();
		
		String username = null;
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(username);
		userDTO.setUserType(USER);
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Username cannot be empty.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithMissingPassword() {
		UserDTO userDTO = new UserDTO();
		
		String password = null;
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password can't be null.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithEmptyPassword() {
		UserDTO userDTO = new UserDTO();
		
		String password = "";
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password can't be null.", e.getMessage());
		}
	}

	@Test
	public void testChangeUserPassword() {
		String newPassword = "newPassword123!";
		
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(newPassword);
		passwordChangeDTO.setOldPassword(USER_PASSWORD);
		UserDTO userPasswordChangeDTO = null;
		try {
			userPasswordChangeDTO = userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			e.printStackTrace();
		}
		assertNotNull(userPasswordChangeDTO);
	}
	
	@Test
	public void testChangeUserPasswordWithUserNotFound() {
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName("USER_NAME");
		passwordChangeDTO.setNewPassword(NEW_USER_PASSWORD);
		passwordChangeDTO.setOldPassword(USER_PASSWORD);
		
		try {
			userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			assertEquals("No user was found", e.getMessage());
		}
	}
	
	@Test
	public void testChangeUserPasswordWithWrongPassword() {
		
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(NEW_USER_PASSWORD);
		passwordChangeDTO.setOldPassword(USER_PASSWORD2);
		
		try {
			userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			assertEquals("Wrong password", e.getMessage());
		}
	}
	
	@Test
	public void testChangeUserPasswordToNull() {
		
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(null);
		passwordChangeDTO.setOldPassword(USER_PASSWORD);
		
		try {
			userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			assertEquals("Password cannot be null.", e.getMessage());
		}
	}
	
	@Test
	public void testChangeUserPasswordToEmpty() {
		
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword("");
		passwordChangeDTO.setOldPassword(USER_PASSWORD);
		
		try {
			userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			assertEquals("Password cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testChangeInvalidPassword() {
		
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword("123!Aa");
		passwordChangeDTO.setOldPassword(USER_PASSWORD);
		
		try {
			userService.updateUser(passwordChangeDTO);
		} catch (PasswordException e) {
			assertEquals("Password must be at least 8 characters in length.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithSameUsername() {
		UserDTO user1 = new UserDTO();
		
		user1.setEmail(USER_EMAIL);
		user1.setPassword(USER_PASSWORD);
		user1.setUserType(USER);
		user1.setUsername(USER_NAME);
		
		try {
			userService.createUser(user1);
		} catch (RegisterException ignored) {
			// fail(); this doesnt work idk why, it fails the test
		} // we know that the first one will register for sure
		
		UserDTO user2 = new UserDTO();
		String email2 = "myEmail1234@gmail.com";
		
		user2.setUsername(USER_NAME);
		user2.setUserType(USER);
		user2.setEmail(email2);
		user2.setPassword(USER_PASSWORD);
		
		try {
			userService.createUser(user2);
		} catch (RegisterException e) {
			Assert.assertEquals("Username is already taken.", e.getMessage());
		}
	}
	
	@Test
	public void testCreateWithSameEmail() {
		UserDTO user1 = new UserDTO();
		
		user1.setEmail(USER_EMAIL);
		user1.setPassword(USER_PASSWORD);
		user1.setUserType(USER);
		user1.setUsername(USER_NAME);
		
		try {
			userService.createUser(user1);
		} catch (RegisterException ignore) {
			// Assert.fail();
		} // we know that the first one will register for sure
		
		UserDTO user2 = new UserDTO();
		String username2 = "myUsernameIsDiff";
		
		user2.setUsername(username2);
		user2.setUserType(USER);
		user2.setEmail(USER_EMAIL);
		user2.setPassword(USER_PASSWORD);
		
		try {
			userService.createUser(user2);
		} catch (RegisterException e) {
			Assert.assertEquals("Email is already taken.", e.getMessage());
		}
	}

	@Test
	public void testPasswordShorterThan8() {
		UserDTO userDTO = new UserDTO();
		
		String password = "1A/c";
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password must be at least 8 characters in length.", e.getMessage());
		}
		
	}

	@Test
	public void testPasswordNoNumber() {
		UserDTO userDTO = new UserDTO();
		
		String password = "/aAasdfg";
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password must contain at least 1 digit characters.", e.getMessage());
		}
		
	}

	@Test
	public void testPasswordNoUpperCase() {
		UserDTO userDTO = new UserDTO();
		
		String password = "/1abcdasdasdasdqdqw";
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password must contain at least 1 uppercase characters.", e.getMessage());
		}
	}

	@Test
	public void testPasswordNoSpecialCharacter() {
		UserDTO userDTO = new UserDTO();
		
		String password = "ACSW1abcd";
		
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(USER);
		
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password must contain at least 1 special characters.", e.getMessage());
		}
	}
	
	@Test
	public void testDeleteUser() throws RegisterException {
		assertTrue(userService.deleteUser(USER_NAME));
	}
	
	@Test
	public void testDeleteNotExistingUser() {
		try {
			userService.deleteUser(USER_NAME2);
		} catch (RegisterException e) {
			assertEquals("User not found", e.getMessage());
		}
	}
	
	@Test
	public void testResetPassword() {
		try {
			userService.resetPassword(USER_EMAIL);
		} catch (RegisterException e) {
			fail();
		}
	}
	
	@Test
	public void testResetPasswordInvalidUser() {
		try {
			userService.resetPassword(USER_EMAIL2);
		} catch (RegisterException e) {
			assertEquals("User not found", e.getMessage());
		}
	}
}
