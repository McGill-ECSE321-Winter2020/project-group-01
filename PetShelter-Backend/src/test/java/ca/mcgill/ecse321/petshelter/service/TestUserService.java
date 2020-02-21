package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Before
    public void clearDatabase() {
        userRepository.deleteAll();
    }
    
    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "myP1+abc";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException ignored) {
            //ignored for now
        }
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        assertEquals(username, allUsers.get(0).getUserName());
    }
    
    @Test
    public void testRegisterWithInvalidEmail(){
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@";
        String password = "myPassword123!";
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);

        try{
            userService.addUser(userDTO);
        } catch (Exception e){
            assertEquals("The provided email is not a valid email address.", e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithMissingAtEmail(){
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmailgmail.com";
        String password = "myPassword123!";
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try{
            userService.addUser(userDTO);
        } catch (Exception e){
            assertEquals("The provided email is not a valid email address.",e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithMissingUsername() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123!";
        String username = null;
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            //ignored for now
            assertEquals("Username cannot be empty.",e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithMissingPassword(){
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = null;
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            System.out.println(e.getMessage());
            assertEquals("Password can't be null.",e.getMessage());
        }
    }
    
    @Test
    public void testChangeUserPassword(){
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername!";
        String newPassword = "newPassword123!";
        UserType userType = USER;
    
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setUserName(username);
        passwordChangeDTO.setNewPassword(newPassword);
        passwordChangeDTO.setOldPassword(password);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try{
            userService.addUser(userDTO);
            userService.changeUserPassword(passwordChangeDTO);
        } catch (RegisterException e){
        
        }
    }
    
    @Test
    public void testChangeUserPasswordToNull() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        String newPassword = null;
        UserType userType = USER;
    
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setUserName(username);
        passwordChangeDTO.setNewPassword(newPassword);
        passwordChangeDTO.setOldPassword(password);
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try{
            userService.addUser(userDTO);
            userService.changeUserPassword(passwordChangeDTO);
        } catch (RegisterException e){
            assertEquals("Password must contain at least 1 special characters.",e.getMessage());
        }
    }
    
    
    @Test
    public void testChangeInvalidPassword() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        String newPassword = "1m3";
        UserType userType = USER;
        
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setUserName(username);
        passwordChangeDTO.setNewPassword(newPassword);
        passwordChangeDTO.setOldPassword(password);
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try {
            userService.addUser(userDTO);
            userService.changeUserPassword(passwordChangeDTO);
        } catch (RegisterException e) {
            assertEquals("Password must contain at least 1 special characters.", e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithSameUsername() {
        UserDTO user1 = new UserDTO();
        String email = "myEmail@gmail.com";
        String password = "myPassword123!";
        String username = "myUsername";
        UserType userType = USER;
        
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setUserType(userType);
        user1.setUsername(username);
        
        try {
			userService.addUser(user1);
		} catch (RegisterException e1) {
fail();
		} //we know that the first one will register for sure
        
        UserDTO user2 = new UserDTO();
        String email2 = "myEmail1234@gmail.com";
        String password2 = "myPassword123!";
        String username2 = "myUsername";
        UserType userType2 = USER;
        
        user2.setUsername(username2);
        user2.setUserType(userType2);
        user2.setEmail(email2);
        user2.setPassword(password2);
        
        try {
            userService.addUser(user2);
        } catch (RegisterException e) {
            assertEquals("Username is already taken.", e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithSameEmail() {
        UserDTO user1 = new UserDTO();
        String email = "myEmail@gmail.com";
        String password = "myPassword123!";
        String username = "myUsername";
        UserType userType = USER;
        
        user1.setEmail(email);
        user1.setPassword(password);
        user1.setUserType(userType);
        user1.setUsername(username);
        
        try {
			User addUser = userService.addUser(user1);
		} catch (RegisterException e1) {
fail();
		} //we know that the first one will register for sure
        
        UserDTO user2 = new UserDTO();
        String email2 = "myEmail@gmail.com";
        String password2 = "myPassword123!";
        String username2 = "myUsernameIsDiff";
        UserType userType2 = USER;
        
        user2.setUsername(username2);
        user2.setUserType(userType2);
        user2.setEmail(email2);
        user2.setPassword(password2);
        
        try {
            userService.addUser(user2);
        } catch (RegisterException e) {
            assertEquals("Email is already taken.", e.getMessage());
        }
    }
    
    //@Size(min = 4, max = 20
    @Test
    public void testPasswordShorterThan8() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "1A/c";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            assertEquals("Password must be at least 8 characters in length.", e.getMessage());
        }
        
    }
    
    @Test
    public void testPasswordNoNumber() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "/aAasdfg";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            assertEquals("Password must contain at least 1 digit characters.", e.getMessage());
        }
        
    }
    
    @Test
    public void testPasswordNoUpperCase() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "/1abcdasdasdasdqdqw";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            assertEquals("Password must contain at least 1 uppercase characters.", e.getMessage());
        }
    }
    
    @Test
    public void testPasswordNoSpecialCharacter() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "ACSW1abcd";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            assertEquals("Password must contain at least 1 special characters.", e.getMessage());
        }
    }
    
    @Test
    public void testDeleteUser() {
        UserDTO userDTO = new UserDTO();
        
        String email = "myEmail@gmail.com";
        String password = "myPassword123!";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
			userService.addUser(userDTO);
		} catch (RegisterException e) {
fail();
		}
        
        User dbUser = userRepository.findUserByUserName(username);
        
        assertEquals(userDTO.getUsername(), dbUser.getUserName());
        
        userService.deleteUser(userDTO);
        
        assertNull(userRepository.findUserByUserName(username));
    }
    
    //i dont know how this can happen though
    @Test
    public void testFailedDeleteUser() {
    
    }
    
}
