package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.DonationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDonationService {
    
    @Autowired
    private DonationService donationService;
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Before
    public void clearDatabase() {
        donationRepository.deleteAll();
    }
    
    /**
     * Creates a new user for the tests
     *
     * @return user with pre-filled values
     */
    public User createUser() {
        String name = "TestUserName";
        String password = "myPassword";
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
    
    
    @Test
    public void testDonate() {
        User user = createUser();
        
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setUser("bob");
        donationDTO.setAmount(12.12);
        donationDTO.setDate(Date.valueOf("2020-02-01"));
        donationDTO.setTime(Time.valueOf("11:11:00"));
        
        Donation donation = donationService.createDonation(donationDTO);
        
        assertEquals(12.12, donation.getAmount());
        
        
    }
}
