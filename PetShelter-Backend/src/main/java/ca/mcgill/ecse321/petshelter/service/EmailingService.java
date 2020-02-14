package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;
import java.sql.Time;
import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailingService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    private String serviceEmail = "${spring.mail.username}";
    
    @Value("${baseurl}")
    private String url;
    
    /**
     * Sends email confirmation for donation
     *
     * @param email    user's email
     * @param username user's username
     * @param amount   user's donation amount
     * @param time     donation time
     * @param date     donation date
     */
    public void donationConfirmationEmail(String email, String username, double amount, Time time, Date date) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(serviceEmail);
        msg.setTo(email);
    
        msg.setSubject("Pet Shelter - Donation Confirmation");
        msg.setText("Hey " + username + " , \n\n" + "Thank you for your donation. We have successfully processed it at " +
                time + " EST " + date + " for an amount of: " + currencyFormat.format(amount) + "\n\nYours, \nPetShelter");
        
        javaMailSender.send(msg);
    }
    
    /**
     * Sends email to confirm user's email address
     *
     * @param email    user's email
     * @param username user's username
     * @param token    randomly generated token for confirmation
     */
    public void userCreationEmail(String email, String username, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(serviceEmail);
        msg.setTo(email);
        msg.setSubject("Pet Shelter - Account Creation Verification for " + username);
        msg.setText("Hey " + username + " ,\n\nPlease confirm your email here: " + url + "registrationConfirmation?token=" + token + "\n\nYours, \nPetShelter");
        javaMailSender.send(msg);
    }
    
    /**
     * Sends temporary password to user
     *
     * @param email    user's email
     * @param tempPw   user's temporary password
     * @param username user's username
     */
    public void userForgotPasswordEmail(String email, String tempPw, String username) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Pet Shelter - Password Reset");
        msg.setText("Hey " + username + " ,\n\nHere is your temporary password " + tempPw + "\n\nYours, \nPetShelter");
        javaMailSender.send(msg);
    }
}
