package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Service to handle login and registration of users.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    
    
    private EmailingService emailingService;
    
    public UserService(EmailingService emailingService) {
        this.emailingService = emailingService;
    }
    
    //TODO create helper methods to shorten the code here
    // register users
    public User addUser(UserDTO user) {
        //check that the email and username are unique
        if (userRepository.findUserByEmail(user.getEmail()) != null)
            throw new RegisterException("Email is already taken.");
        if (userRepository.findUserByUserName(user.getUsername()) != null)
            throw new RegisterException("Username is already taken.");
        //create the user and set its attributes
        User user1 = new User();
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setUserName(user.getUsername());
        user1.setUserType(user.getUserType());
        String token = jwtTokenProvider.createToken(user1.getUserName());
        user1.setApiToken(token);
        //save it
        userRepository.save(user1);
        // Send email
        emailingService.userCreationEmail(user.getEmail(), user.getUsername(), token);
        return user1;
    }
    
    // method that only checks if a user could be logged in
    public User loginUser(UserDTO user) throws LoginException {
        // if no user is found by its username, it does not exist
        User user1 = userRepository.findUserByUserName(user.getUsername());
        if (user1 == null) {
            throw new LoginException("Username not found");
        }
        // if the password doesnt match the saved one
        String expectedPW = user1.getPassword();
        if (!passwordEncoder.matches(user.getPassword(), expectedPW)) {
            throw new LoginException("Incorrect password");
        }
        // if the user has not verified their account through email
        if (!user1.isIsEmailValidated())
            throw new LoginException("Account not verified");
        return user1;
    }
    
    public String generateRandomPassword() {
        String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(1, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(1);
        String totalChars = RandomStringUtils.randomAlphanumeric(6);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
    
}
