package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class TestUserServicesMockito {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    
    private  static final String USER_KEY = "TestPerson";
    
    @BeforeEach
    public void setMockOutput(){
        lenient().when(userRepository.findUserByUserName(anyString())).thenAnswer(
                (InvocationOnMock invocation)->{
                    if(invocation.getArgument(0).equals(USER_KEY)){
                        User user = new User();
                        user.setUserName(USER_KEY);
                        return user;
                    } else{
                        return null;
                    }
                });
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
        
    }
    
    @Test
    public void testCreatePerson(){
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
            // ignored for now
        }
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        assertEquals(username, allUsers.get(0).getUserName());
    }
    
}
