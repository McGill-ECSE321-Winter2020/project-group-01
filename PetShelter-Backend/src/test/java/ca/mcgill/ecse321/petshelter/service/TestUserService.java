package ca.mcgill.ecse321.petshelter.service;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//TODO, fill out the rest of the test cases

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
        String password = "myPassword123";
        String username = "myUsername";
        UserType userType = USER;
        
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
        
        try {
            userService.addUser(userDTO);
        } catch (RegisterException e) {
            //ignored for now
        }
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        assertEquals(username, allUsers.get(0).getUserName());
    }
    
    @Test
    public void testRegisterWithInvalidEmail(){
        String error = "Could not parse mail; nested exception is javax.mail.internet.AddressException: Missing domain in string ``myEmail@''";
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@";
        String password = "myPassword123";
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);

        try{
            userService.addUser(userDTO);
        } catch (Exception e){
            assertEquals(error, e.getMessage());
        }
    }
    
    @Test
    public void testRegisterWithMissingAtEmail(){
        String error = "Failed messages: javax.mail.SendFailedException: Invalid Addresses"; // can only pattern match on the error because of how the error is handled in spring
        Pattern pattern = Pattern.compile(error);
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmailgmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try{
            userService.addUser(userDTO);
        } catch (Exception e){
            Matcher matcher = pattern.matcher(e.getMessage());
            assertTrue(matcher.find());
        }
    }
    
    @Test
    public void testRegisterWithMissingUsername() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
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
            assertEquals("Username can't be null.",e.getMessage());
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
            assertEquals("Password can't be null.",e.getMessage());
        }
    }
    
    @Test
    public void testChangeUserPassword(){
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        String newPassword = "newPassword123";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        //todo
    }
    
    @Test
    public void testChangeUserPasswordToNull() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        String newPassword = null;
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        //todo
    }
    
    
    @Test
    public void testChangeInvalidPassword() {
        UserDTO userDTO = new UserDTO();
    
        String email = "myEmail@gmail.com";
        String password = "myPassword123";
        String username = "myUsername";
        String newPassword = "123";
        UserType userType = USER;
    
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        //todo
    }
    
}
