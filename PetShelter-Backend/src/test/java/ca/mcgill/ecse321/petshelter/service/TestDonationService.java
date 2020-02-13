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
import java.util.List;

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
    
    //todo, check if this is ok
    private String name = "TestUserName";
    
    @Before
    public void createUser() {
        
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
    }
    
    
    @Test
    public void testDonate() {
        DonationDTO donationDTO = new DonationDTO();
    
        String userName = name;
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 11.22;
        donationDTO.setUser(userName);
        donationDTO.setAmount(amount);
        donationDTO.setDate(date);
        donationDTO.setTime(time);
    
        try {
            donationService.createDonation(donationDTO);
        } catch (IllegalArgumentException ignored) {
        }
    
        List<Donation> allDonations = donationService.getAllDonations();
        assertEquals(userName, allDonations.get(0).getUser().getUserName());
    }
}
