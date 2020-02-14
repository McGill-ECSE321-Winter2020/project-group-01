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

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.Assert.assertEquals;

//TODO, fill out the rest of the test cases

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserServices {
    
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
}
