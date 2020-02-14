package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.UserType;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDonationService {
    
    @Autowired
    private DonationService donationService;
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Before
    public void clearDatabase() {
        donationRepository.deleteAll();
        userRepository.deleteAll();
    }
    
    private String name = "TestUserName";
    
    public void createUser() {
        UserDTO userDTO = new UserDTO();
        String password = "myPassword";
        String email = "TestUserName@gmail.com";
        UserType userType = UserType.USER;
        
        userDTO.setUsername(name);
        userDTO.setUserType(userType);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userService.addUser(userDTO);
    }
    
    //Regular use case
    @Test
    public void testDonation() {
        createUser();
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
        } catch (DonationException ignored) {
        }
        
        List<Donation> allDonations = donationService.getAllDonations();
        assertEquals(userName, allDonations.get(0).getUser().getUserName());
        assertEquals(amount, allDonations.get(0).getAmount());
        assertEquals(date, allDonations.get(0).getDate());
    }
    
    //Anonymous donation
    @Test
    public void anonymousDonation() {
        DonationDTO donationDTO = new DonationDTO();
        String username = "bobby";
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 11.22;
        
        donationDTO.setUser(username);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException ignored) {
        
        }
        
        List<Donation> allDonations = donationService.getAllDonations();
        assertNull(allDonations.get(0).getUser());
    }
    
    //negative donation amount
    @Test
    public void negativeDonation() {
        createUser();
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = -11.22;
        
        donationDTO.setUser(name);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation amount can't be less than 0$", e.getMessage());
        }
    }
    
    //zero donation amount, edge case
    @Test
    public void zeroDonation() {
        createUser();
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        double amount = 0;
        
        donationDTO.setUser(name);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(amount);
        
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation amount can't be less than 0$", e.getMessage());
        }
    }
    
    //no donation amount
    @Test
    public void nullDonation() {
        createUser();
        DonationDTO donationDTO = new DonationDTO();
        Date date = Date.valueOf("2020-01-22");
        Time time = Time.valueOf("11:22:00");
        
        donationDTO.setUser(name);
        donationDTO.setTime(time);
        donationDTO.setDate(date);
        donationDTO.setAmount(null);
        try {
            donationService.createDonation(donationDTO);
        } catch (DonationException e) {
            assertEquals("Donation can't be null!", e.getMessage());
        }
    }
}
