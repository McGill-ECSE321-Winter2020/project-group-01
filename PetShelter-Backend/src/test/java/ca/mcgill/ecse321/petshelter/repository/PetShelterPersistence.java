package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class PetShelterPersistence {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;
    
    /*
    
    NOTE: testDeleteDonation will not be implemented as previously discussed in our design meeting.
    It is to keep as an audit purposes
    
    
     */
    
    /**
     * clears the database before every run
     */
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        advertisementRepository.deleteAll();
        applicationRepository.deleteAll();
        commentRepository.deleteAll();
        donationRepository.deleteAll();
        forumRepository.deleteAll();
        userRepository.deleteAll();
        petRepository.deleteAll();
    }
    
    /**
     * Creates a new user for the tests
     *
     * @return user with pre-filled values
     */
    public User createUser() {
        String name = "TestUserName";
        String password = "myPassword1!";
        boolean emailValid = true;
        String email = "TestUserName@gmail.com";
        String apiToken = "token112";
        
        User user = new User();
        user.setUserName(name);
        user.setPassword(password);
        user.setIsEmailValidated(emailValid);
        user.setEmail(email);
        user.setApiToken(apiToken);
        
        userRepository.save(user);
        return user;
    }
    
    /**
     * Creates a new advertisement for the tests
     *
     * @return advertisement with pre-filled values
     */
    public Advertisement createAdvertisement() {
        String description = "myDescription";
        String title = "myTitle";
        boolean isFulfiled = true;
        
        Advertisement advertisement = new Advertisement();
        
        advertisement.setDescription(description);
        advertisement.setIsFulfilled(isFulfiled);
        advertisement.setTitle(title);
        
        advertisementRepository.save(advertisement);
        return advertisement;
    }
    
    /**
     * Creates a new comment for the tests
     *
     * @return comment with pre-filled values
     */
    public Comment createComment() {
        Date postedDate = Date.valueOf("2015-03-21");
        String commentText = "this is a comment";
        Time time = Time.valueOf("10:22:03");
        
        User user = createUser();
        Comment comment = new Comment();
        
        comment.setDatePosted(postedDate);
        comment.setText(commentText);
        comment.setTime(time);
        comment.setUser(user);
        
        commentRepository.save(comment);
        return comment;
    }
    
    @Test
    public void testPersistAndLoadUser() {
        //parameters for users
        String name = "TestUserNamee";
        String password = "myPassword1!";
        boolean emailValid = true;
        String email = "TestUserName@gmail.com";
        String apiToken = "token112";
        
        User user = new User();
        
        // sets everything
        user.setUserName(name);
        user.setPassword(password);
        user.setIsEmailValidated(emailValid);
        user.setEmail(email);
        user.setApiToken(apiToken);
        
        userRepository.save(user);
        user = null;
        
        //asserts if everything can be retrieved from database
        user = userRepository.findUserByUserName(name);
        assertNotNull(user);
        assertEquals(name, user.getUserName());
        assertEquals(password, user.getPassword());
        assertEquals(emailValid, user.isIsEmailValidated());
        assertEquals(apiToken, user.getApiToken());
    }
    
    @Test
    public void testPersistAndLoadComment() {
        //comments details
        Date postedDate = Date.valueOf("2015-03-21");
        String commentText = "this is a comment";
        Time time = Time.valueOf("10:22:03");
        
        User user = createUser();
        Comment comment = new Comment();
        
        //sets everything
        comment.setDatePosted(postedDate);
        comment.setText(commentText);
        comment.setTime(time);
        comment.setUser(user);
        
        commentRepository.save(comment);
        comment = null;
        
        //asserts if everything can be retrieved from database
        comment = commentRepository.findCommentByUserAndText(user, commentText);
        assertNotNull(comment);
        assertEquals(commentText, comment.getText());
        
    }
    
    @Test
    public void testPersistAndLoadAdvertisement() {
        //Advertisement details
        String description = "myDescription";
        String title = "myTitle";
        boolean isFulfiled = true;
        
        Advertisement advertisement = new Advertisement();
        
        //sets advertisement
        advertisement.setDescription(description);
        advertisement.setIsFulfilled(isFulfiled);
        advertisement.setTitle(title);
        
        advertisementRepository.save(advertisement);
        advertisement = null;
        
        //asserts if everything can be retrieved from database
        advertisement = advertisementRepository.findAdvertisementByTitle(title);
        assertNotNull(advertisement);
        assertEquals(title, advertisement.getTitle());
        assertEquals(description, advertisement.getDescription());
        assertEquals(isFulfiled, advertisement.isIsFulfilled());
    }
    
    @Test
    public void testPersistAndLoadApplication() {
        //Application details
        User applicant = createUser();
        String description = "myDescription";
        
        Advertisement ad = new Advertisement();
        advertisementRepository.save(ad);
        boolean isAccepted = false;
        
        AdoptionApplication application = new AdoptionApplication();
        application.setIsAccepted(isAccepted);
        application.setAdvertisement(ad);
        application.setUser(applicant);
        application.setDescription(description);
        
        applicationRepository.save(application);
        
        //asserts if everything can be retrieved from database
        application = applicationRepository.findApplicationByUserAndAdvertisement(applicant, ad);
        assertNotNull(application);
        assertEquals(applicant.getUserName(), application.getUser().getUserName());
        assertEquals(ad.getTitle(), application.getAdvertisement().getTitle());
        assertEquals(description, application.getDescription());
        assertEquals(isAccepted, application.isIsAccepted());
    }
    
    @Test
    public void testPersistAndLoadPet() {
        // user info
        User user = createUser();
        
        // pet info
        String petName = "TestPetName";
        Date birthDate = Date.valueOf("2015-03-31");
        String species = "Dog";
        String breed = "Labrador";
        Pet pet = new Pet();
        pet.setDateOfBirth(birthDate);
        pet.setName(petName);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setGender(Gender.FEMALE);
        HashSet<Pet> pets = new HashSet<Pet>();
        user.setPets(pets);
        petRepository.save(pet);
        userRepository.save(user);
        

        
        //asserts if everything can be retrieved from database
        pet = petRepository.findPetByNameAndAdvertisement(petName, pet.getAdvertisement());
        assertNotNull(pet);
        assertEquals(petName, pet.getName());
        assertEquals(birthDate, pet.getDateOfBirth());
        assertEquals(breed, pet.getBreed());
        
    }
    
    @Test
    public void testPersistAndLoadDonation() {
        //donation details
        User user = createUser();
        Date donationDate = Date.valueOf("2015-03-31");
        Time donationTime = Time.valueOf("10:22:03");
        double amount = 122.2;
        
        Donation donation = new Donation();
        
        donation.setAmount(amount);
        donation.setDate(donationDate);
        donation.setTime(donationTime);
        donation.setUser(user);
        
        donationRepository.save(donation);
        donation = null;
        
        //asserts if everything can be retrieved from database
        donation = donationRepository.findDonationByUserAndAmount(user, amount);
        assertNotNull(donation);
        assertEquals(donationDate, donation.getDate());
        assertEquals(user.getUserName(), donation.getUser().getUserName());
        assertEquals(amount, donation.getAmount(), 0.01);
        
    }
    
    @Test
    public void testPersistAndLoadForum() {
        //forum details
        String title = "myTitlee";
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        Comment comment3 = createComment();
        Set<Comment> commentSet = new HashSet<>();
        commentSet.add(comment1);
        commentSet.add(comment2);
        commentSet.add(comment3);
        
        User user1 = createUser();
        User user2 = createUser();
        HashSet<User> userSet = new HashSet<>();
        userSet.add(user1);
        userSet.add(user2);
        
        Forum forum = new Forum();
        forum.setComments(commentSet);
        forum.setSubscribers(userSet);
        forum.setTitle(title);
    
        forumRepository.save(forum);
    
        forum = null;
    
        //asserts if everything can be retrieved from database
        forum = forumRepository.findForumByTitle(title);
        assertNotNull(forum);
        assertEquals(title, forum.getTitle());
    }
    
    @Test
    public void testDeleteUser() {
        String name = "TestUserNamee!1";
        String password = "myPassword1!";
        boolean emailValid = true;
        String email = "TestUserName@gmail.com";
        String apiToken = "token112";
        
        User user = new User();
        
        // sets everything
        user.setUserName(name);
        user.setPassword(password);
        user.setIsEmailValidated(emailValid);
        user.setEmail(email);
        user.setApiToken(apiToken);
        
        userRepository.save(user);
        
        User dbUser = userRepository.findUserByUserName(user.getUserName());
        
        //ensure the user is in the database
        assertEquals(user.getUserName(), dbUser.getUserName());
        
        //delete the user by ID
        // userRepository.deleteById(user.getId());
        userRepository.deleteById(user.getId());
        //now it should be null
        assertNull(userRepository.findUserByUserName(user.getUserName()));
    }

    @Test
    public void testDeleteAdvertisement() {
        Advertisement advertisement = createAdvertisement();
        
        Advertisement dbAd = advertisementRepository.findAdvertisementByTitle(advertisement.getTitle());
        
        assertEquals(advertisement.getTitle(), dbAd.getTitle());
        
        advertisementRepository.deleteById(advertisement.getId());
        
        assertNull(advertisementRepository.findAdvertisementByTitle(advertisement.getTitle()));
    }
    
    @Test
    public void testDeleteAdoptionApplication() {
        User applicant = createUser();
        String description = "myDescription";
        
        Advertisement ad = new Advertisement();
        advertisementRepository.save(ad);
        boolean isAccepted = false;
        
        AdoptionApplication application = new AdoptionApplication();
        application.setIsAccepted(isAccepted);
        application.setAdvertisement(ad);
        application.setUser(applicant);
        application.setDescription(description);
        
        applicationRepository.save(application);
        
        AdoptionApplication dbApplication = applicationRepository.findApplicationByUserAndAdvertisement(applicant, ad);
        
        assertEquals(application.getUser().getUserName(), dbApplication.getUser().getUserName());
        
        applicationRepository.deleteById(application.getId());
        
        assertNull(applicationRepository.findApplicationByUserAndAdvertisement(applicant, ad));
    }
    
    @Test
    public void testDeleteComment() {
        Comment comment = createComment();
        Comment dbComment = commentRepository.findCommentByUserAndText(comment.getUser(), comment.getText());
        
        assertEquals(comment.getText(), dbComment.getText());
        
        commentRepository.deleteById(comment.getId());
        
        assertNull(commentRepository.findCommentByUserAndText(comment.getUser(), comment.getText()));
    }
    
    @Test
    public void testDeleteForum() {
        String title = "myTitlee";
        Comment comment1 = createComment();
        Comment comment2 = createComment();
        Comment comment3 = createComment();
        Set<Comment> commentSet = new HashSet<>();
        commentSet.add(comment1);
        commentSet.add(comment2);
        commentSet.add(comment3);
        
        User user1 = createUser();
        User user2 = createUser();
        HashSet<User> userSet = new HashSet<>();
        userSet.add(user1);
        userSet.add(user2);
        
        Forum forum = new Forum();
        forum.setComments(commentSet);
        forum.setSubscribers(userSet);
        forum.setTitle(title);
        
        forumRepository.save(forum);
        
        Forum dbForum = forumRepository.findForumByTitle(title);
        
        assertEquals(title, dbForum.getTitle());
        
        forumRepository.deleteById(forum.getId());
        
        assertNull(forumRepository.findForumByTitle(forum.getTitle()));
    }
    @Test
    public void testDeletePet() {
        String name = "TestUserNamee!";
        String password = "myPassword1!";
        boolean emailValid = true;
        String email = "TestUserName@gmail.com";
        String apiToken = "token112";
        
        User user = new User();
        
        // sets everything
        user.setUserName(name);
        user.setPassword(password);
        user.setIsEmailValidated(emailValid);
        user.setEmail(email);
        user.setApiToken(apiToken);
        
        userRepository.save(user);
        
        // pet info
        String petName = "TestPetNameBob";
        Date birthDate = Date.valueOf("2015-03-31");
        String species = "Dog";
        String breed = "Labrador";
        Pet pet = new Pet();
        pet.setDateOfBirth(birthDate);
        pet.setName(petName);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setGender(Gender.FEMALE);
        HashSet<Pet> pets = new HashSet<Pet>();
        user.setPets(pets);
        petRepository.save(pet);
        userRepository.save(user);
        
        
        //asserts if everything can be retrieved from database
        // Pet dbPet = petRepository.findPetByName(petName);
        Pet dbPet = petRepository.findPetByNameAndAdvertisement(petName, pet.getAdvertisement());
        assertEquals(pet.getName(), dbPet.getName());
        
        petRepository.deleteById(pet.getId());
        
        assertNull(petRepository.findPetByNameAndAdvertisement(petName, pet.getAdvertisement()));
    }
}
