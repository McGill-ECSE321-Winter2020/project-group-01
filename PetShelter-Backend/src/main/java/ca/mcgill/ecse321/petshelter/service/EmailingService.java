package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;

@Service
public class EmailingService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Sends email confirmation for donation
     *
     * @param donationDTO donation instance of user
     */
    public void donationConfirmation(DonationDTO donationDTO) {
        User donor = userRepository.findUserByUserName(donationDTO.getUsername());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("${spring.mail.username}");
        msg.setTo(donor.getEmail());
        
        msg.setSubject("Donation Confirmation");
        msg.setText("Hey " + donationDTO.getUsername() + " , \n\n" + "Thank you for your donation. We have successfully processed it at " +
                donationDTO.getTime() + " EST " + donationDTO.getDate() + " for an amount of: " + currencyFormat.format(donationDTO.getAmount()) + "\n\nYours, \nPetShelter");
        
        javaMailSender.send(msg);
    }
    
}
