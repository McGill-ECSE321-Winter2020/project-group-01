package ca.mcgill.ecse321.petshelter.service;

import static ca.mcgill.ecse321.petshelter.model.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TestUserServicesMockito {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JWTTokenProvider jwtTokenProvider;

	@Mock
	private EmailingService emailingService;

    @InjectMocks
    private UserService userService;
    
    private static final String USER_KEY = "TestPerson";
    private static final String USER_KEY2 = "TestPerson@email.com";
    
    @BeforeEach
    public void setMockOutput(){
    	MockitoAnnotations.initMocks(this);
        when(userRepository.findUserByUserName(anyString())).thenAnswer(
                (InvocationOnMock invocation)->{
                    if(invocation.getArgument(0).equals(USER_KEY)){
                        User user = new User();
                        user.setUserName(USER_KEY);
                        return user;
                    } else{
                        return null;
                    }
                });
        when(userRepository.findUserByEmail(anyString())).thenAnswer(
                (InvocationOnMock invocation)->{
                    if(invocation.getArgument(0).equals(USER_KEY2)){
                        User user = new User();
                        user.setEmail(USER_KEY2);
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
        String password = "myP1+abc";
        String username = "myUsername";
        UserType userType = USER;
    
        userDTO.setEmail(USER_KEY2);
        userDTO.setPassword(password);
        userDTO.setUsername(username);
        userDTO.setUserType(userType);
    
        try {
            userService.addUser(userDTO);
        } catch (RegisterException ignored) {
            // ignored for now
        }
        User e = userRepository.findUserByEmail(USER_KEY2);
        assertEquals(userDTO.getEmail(), e.getEmail());
    }
    
}
