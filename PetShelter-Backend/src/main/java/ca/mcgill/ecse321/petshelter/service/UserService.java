package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.PasswordChangeDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.*;
import ca.mcgill.ecse321.petshelter.repository.*;
import ca.mcgill.ecse321.petshelter.service.exception.PasswordException;
import ca.mcgill.ecse321.petshelter.service.exception.RegisterException;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.EmailingService;
import ca.mcgill.ecse321.petshelter.service.extrafeatures.JWTTokenProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Service to handle login and registration of users.
 */
@Service
public class UserService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    
    private EmailingService emailingService;
    
    public UserService(EmailingService emailingService) {
        this.emailingService = emailingService;
    }
    
    // converts a user into a userdto
    static UserDTO userToDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUserName());
        userDto.setUserType(user.getUserType());
        userDto.setPicture(user.getPicture());
        userDto.setToken(user.getApiToken());
        return userDto;
    }
    
    /**
     * Creates a user if the input is valid and sends an email to the specified
     * email address.
     *
     * @param user
     * @return
     */
    
    public UserDTO createUser(UserDTO user) throws RegisterException {
        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            throw new RegisterException("Password can't be null.");
        }
        String validationError = isUserDtoValid(user);
        if (validationError != null) {
            throw new RegisterException(validationError);
        }
        // check that the email and username are unique
        if (userRepository.findUserByEmail(user.getEmail()) != null)
            throw new RegisterException("Email is already taken.");
        if (userRepository.findUserByUserName(user.getUsername()) != null)
            throw new RegisterException("Username is already taken.");
        // create the user and set its attributes
        User user1 = new User();
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setUserName(user.getUsername());
        user1.setUserType(user.getUserType());
        String token = jwtTokenProvider.createToken(user1.getUserName());
        user1.setApiToken(token);
        // if the user is an admin, no need to validate (there is only one admin)
        if (user.getUserType().equals(UserType.ADMIN)) {
            user1.setIsEmailValidated(true);
        }
        // otherwise a validation email must be sent to the user
        else {
            emailingService.userCreationEmail(user.getEmail(), user.getUsername(), token);
        }
        // save it
        userRepository.save(user1);
        return userToDto(user1);
    }
    
    /**
     * Generates a strong temporary password to be used in case of password reset.
     *
     * @return
     */
    public String generateRandomPassword() {
        String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(1, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(1);
        String totalChars = RandomStringUtils.randomAlphanumeric(6);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
    
    /**
     * Validates the UserDto it is given. Email must be an email, and fields must
     * not be empty. Returns null if no error is found. Returns an error message if
     * a violation is found.
     *
     * @param userDto
     * @return
     */
    private String isUserDtoValid(UserDTO userDto) {
        // check if input is valid (email is an email, email and username are not empty)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);
        for (ConstraintViolation<UserDTO> violation : violations) {
            return violation.getMessage();
        }
        return null;
    }
    
    /**
     * Validates the PasswordChangeDto it is given. The new password must satisfy
     * constraitns. Returns null if no error is found.
     *
     * @param passwordChangeDto
     * @return errors if any
     */
    private String isPasswordChangeValid(PasswordChangeDTO passwordChangeDto) {
        // check if input is valid (new password satisfies constraints)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(passwordChangeDto);
        for (ConstraintViolation<PasswordChangeDTO> violation : violations) {
            return violation.getMessage();
        }
        return null;
    }
    
    
    //todo, have to add all the @Mock for delete user to work....
    
    /**
     * This is the update method; only the password can be updated (design
     * decision).
     *
     * @param passwordDto
     * @return
     */
    public UserDTO updateUser(PasswordChangeDTO passwordDto) throws PasswordException {
        if (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().trim().length() == 0) {
            throw new PasswordException("Password cannot be null.");
        }
        // check if new password is valid
        String constraintViolation = isPasswordChangeValid(passwordDto);
        if (constraintViolation != null) {
            throw new PasswordException(constraintViolation);
        }
        User user = userRepository.findUserByUserName(passwordDto.getUserName());
        if (user == null) {
            throw new PasswordException("No user was found");
        }
        // the old password must be correct
        if (!passwordDto.getOldPassword().equals(user.getPassword())) {
            throw new PasswordException("Wrong password");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        return userToDto(user);
    }
    
    /**
     * Deletes a user. Returns false if the user could not be deleted.
     *
     * @param username username to delete
     * @return if we can find the user
     */
    public boolean deleteUser(String username) throws RegisterException {
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            throw new RegisterException("User not found");
        }
        List<Donation> donations = donationRepository.findAllByUser(user);
        for (Donation donation : donations) {
            donation.setUser(null);
        }
        
        List<Forum> forumList = forumRepository.findForumByAuthorUserName(username);
        for (Forum forum : forumList) {
            forum.setAuthor(null);
            Set<User> subs = forum.getSubscribers();
            subs.remove(user);
        }
        
        List<Comment> commentList = commentRepository.findCommentByUserUserName(username);
        for (Comment comment : commentList) {
            comment.setUser(null);
        }
        
        applicationRepository.deleteAdoptionApplicationsByUserUserName(username);
        //petRepository.deletePetsByUserUserName(username);
        if (user.getPets() != null) { //this is needed for the tests to pass
            user.getPets().clear(); //check if this will work
        }
        
        userRepository.deleteById(user.getId());
        return true;
    }
    
    public String resetPassword(String email) throws RegisterException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new RegisterException("User not found");
        }
        String tmpPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(tmpPassword));
        userRepository.save(user);
        return tmpPassword;
    }
}
