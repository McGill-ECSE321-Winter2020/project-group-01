package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.DonationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.DonationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Services to handle the creation of donations.
 *
 * @author dingm
 */
@Service
public class DonationService {
    
    @Autowired
    DonationRepository donationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    /**
     * @return list of all the donations
     */
    @Transactional
    public List<DonationDTO> getAllDonations() {
        return toList(donationRepository.findAll()).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    /**
     * @param name   user's username
     * @param amount amount donated
     * @return donation based on name and amount
     */
    @Transactional
    public DonationDTO getDonation(String name, double amount) {
        return convertToDto(donationRepository.findDonationsByUserUserNameAndAmount(name, amount));
    }
    
    /**
     * @param name user's username
     * @return all the donations made by that username
     */
    @Transactional
    public List<DonationDTO> getAllUserDonations(String name) {
        if(name == null ||donationRepository.findAllByUser(userRepository.findUserByUserName(name)) == null ){
            throw new DonationException("Donations not found");
        }
        return toList(donationRepository.findAllByUser(userRepository.findUserByUserName(name))).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
    
    /**
     * Creates a donation and saves it in database
     *
     * @param donationDTO donation dto from controller
     * @return donation object
     */
    @Transactional
    public DonationDTO createDonation(DonationDTO donationDTO) {
        //condition checks
        if (donationDTO.getAmount() == null) {
            throw new DonationException("Donation can't be null!");
        }
        if (donationDTO.getAmount() < 0.00) {
            throw new DonationException("Donation amount can't be less than 0$");
        }
        if (donationDTO.getAmount() >= 999999999.00) {
            throw new DonationException("The amount is too large. Please make 2 donations!");
        }
    
        // System.out.println(donationDTO.toString());
        User user = userRepository.findUserByUserName(donationDTO.getUsername());
        Donation donation = new Donation();
        donation.setUser(user);
        donation.setTime(new Time(System.currentTimeMillis()));
        donation.setDate(new Date(System.currentTimeMillis()));
        donation.setAmount(Math.round(donationDTO.getAmount() * 100.0) / 100.0);//trim it down to 2 decimal points
        donationRepository.save(donation);
        //  System.out.println(donation.toString());
        return convertToDto(donation);
    }
    
    /**
     * Converts donation object to donation dto
     *
     * @param donation donation object
     * @return donation dto
     */
    public DonationDTO convertToDto(Donation donation) {
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setDate(donation.getDate());
        donationDTO.setTime(donation.getTime());
        donationDTO.setAmount(donation.getAmount());
        
        // need to split them because if the user provides and email but is not registered, we will only send them a confirmation email
        try {
            donationDTO.setUser(donation.getUser().getUserName());
        } catch (NullPointerException e) {
            donationDTO.setUser(null); // occurs when it is an anonymous donor, no account.
        }
        
        try {
            donationDTO.setEmail(donation.getUser().getEmail());
        } catch (NullPointerException e){
            donationDTO.setEmail(null);
        }
        return donationDTO;
    }
}
