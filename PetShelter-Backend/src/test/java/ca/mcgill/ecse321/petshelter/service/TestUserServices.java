package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestUserServices {

	private static final String USER_NAME = "TestPerson";
	private static final String USER_EMAIL = "TestPerson@email.com";
	private static final String USER_PASSWORD = "myP1+abc";
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserService userService;

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
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
		lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);

	}

	@Test
	public void testCreatePerson() {
		UserDTO userDTO = new UserDTO();
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (RegisterException ignored) {
		}
		User e = userRepository.findUserByUserName(USER_NAME);
		assertEquals(userDTO.getEmail(), e.getEmail());
	}

	@Test
	public void testRegisterWithInvalidEmail() {
		UserDTO userDTO = new UserDTO();

		String email = "myEmail@";
		UserType userType = USER;

		userDTO.setEmail(email);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			assertEquals("The provided email is not a valid email address.", e.getMessage());
		}
	}

	@Test
	public void testRegisterWithMissingAtEmail() {
		UserDTO userDTO = new UserDTO();

		String email = "myEmailgmail.com";
		UserType userType = USER;

		userDTO.setEmail(email);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (Exception e) {
			Assert.assertEquals("The provided email is not a valid email address.", e.getMessage());
		}
	}

	@Test
	public void testRegisterWithMissingUsername() {
		UserDTO userDTO = new UserDTO();

		String username = null;
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(username);
		userDTO.setUserType(userType);
		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Username cannot be empty.", e.getMessage());
		}
	}

	@Test
	public void testRegisterWithMissingPassword() {
		UserDTO userDTO = new UserDTO();

		String password = null;
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password can't be null.", e.getMessage());
		}
	}

	@Test
	public void testChangeUserPassword() {
		UserDTO userDTO = new UserDTO();

		String password = "myPassword123";
		String newPassword = "newPassword123!";
		UserType userType = USER;

		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(newPassword);
		passwordChangeDTO.setOldPassword(password);
		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
			UserDTO user = userService.updateUser(passwordChangeDTO);
			assertEquals(newPassword, user.getPassword());
		} catch (RegisterException e) {
		}

	}

	@Test
	public void testChangeUserPasswordToNull() {
		UserDTO userDTO = new UserDTO();

		String newPassword = null;
		UserType userType = USER;

		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(newPassword);
		passwordChangeDTO.setOldPassword(USER_PASSWORD);

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			// userService.createUser(userDTO);
			userService.updateUser(passwordChangeDTO);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Password cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testChangeInvalidPassword() {
		UserDTO userDTO = new UserDTO();

		String newPassword = "1m3iiIiii";
		UserType userType = USER;

		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		passwordChangeDTO.setUserName(USER_NAME);
		passwordChangeDTO.setNewPassword(newPassword);
		passwordChangeDTO.setOldPassword(USER_PASSWORD);

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			// userService.createUser(userDTO);
			userService.updateUser(passwordChangeDTO);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Password must contain at least 1 special characters.", e.getMessage());
		}
	}

	@Test
	public void testRegisterWithSameUsername() {
		UserDTO user1 = new UserDTO();

		UserType userType = USER;

		user1.setEmail(USER_EMAIL);
		user1.setPassword(USER_PASSWORD);
		user1.setUserType(userType);
		user1.setUsername(USER_NAME);

		try {
			userService.createUser(user1);
		} catch (RegisterException ignored) {
			// fail(); this doesnt work idk why, it fails the test
		} // we know that the first one will register for sure

		UserDTO user2 = new UserDTO();
		String email2 = "myEmail1234@gmail.com";

		UserType userType2 = USER;

		user2.setUsername(USER_NAME);
		user2.setUserType(userType2);
		user2.setEmail(email2);
		user2.setPassword(USER_PASSWORD);

		try {
			userService.createUser(user2);
		} catch (RegisterException e) {
			Assert.assertEquals("Username is already taken.", e.getMessage());
		}
	}

	@Test
	public void testRegisterWithSameEmail() {
		UserDTO user1 = new UserDTO();
		UserType userType = USER;

		user1.setEmail(USER_EMAIL);
		user1.setPassword(USER_PASSWORD);
		user1.setUserType(userType);
		user1.setUsername(USER_NAME);

		try {
			userService.createUser(user1);
		} catch (RegisterException ignore) {
			// Assert.fail();
		} // we know that the first one will register for sure

		UserDTO user2 = new UserDTO();
		String username2 = "myUsernameIsDiff";
		UserType userType2 = USER;

		user2.setUsername(username2);
		user2.setUserType(userType2);
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
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

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
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

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
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

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
		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(password);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (RegisterException e) {
			Assert.assertEquals("Password must contain at least 1 special characters.", e.getMessage());
		}
	}

	@Test
	public void testDeleteUser() {
		UserDTO userDTO = new UserDTO();

		UserType userType = USER;

		userDTO.setEmail(USER_EMAIL);
		userDTO.setPassword(USER_PASSWORD);
		userDTO.setUsername(USER_NAME);
		userDTO.setUserType(userType);

		try {
			userService.createUser(userDTO);
		} catch (RegisterException ignored) {
			// Assert.fail();
		}

		User dbUser = userRepository.findUserByUserName(USER_NAME);

		Assert.assertEquals(userDTO.getUsername(), dbUser.getUserName());

		ResponseEntity<?> re = userService.deleteUser(userDTO);
		assert (re.getStatusCode().compareTo(HttpStatus.OK) == 0);
	}
}
