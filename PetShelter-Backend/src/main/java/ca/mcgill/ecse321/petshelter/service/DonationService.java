package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.DonationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonationService {
    
    @Autowired
    DonationRepository donationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public User getUser(String name) {
        return userRepository.findUserByUserName(name);
    }
    
    @Transactional
    public List<Donation> getAllUsers() {
        return toList(donationRepository.findAll());
    }
    
    @Transactional
    public Donation getDonation(User name, double amount) {
        return donationRepository.findDonationByUserAndAmount(name, amount);
    }
    
    @Transactional
    public List<Donation> getAllDonations(User name) {
        return toList(donationRepository.findAll());
    }
    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
    
    @Transactional
    public Donation createDonation(DonationDTO donationDTO) {
        //condition checks
        if (donationDTO.getAmount() < 0.00) {
            throw new IllegalArgumentException("Donation amount can't be less than 0$");
        }
        if (donationDTO.getAmount() == null) {
            throw new IllegalArgumentException("Donation can't be null!");
        }
    
        System.out.println(donationDTO.toString());
        User user = userRepository.findUserByUserName(donationDTO.getUsername());
        Donation donation = new Donation();
        donation.setUser(user);
        donation.setTime(donationDTO.getTime());
        donation.setDate(donationDTO.getDate());
        donation.setAmount(Math.round(donationDTO.getAmount() * 100.0) / 100.0);//trim it down to 2 decimal points
        donationRepository.save(donation);
        System.out.println(donation.toString());
        return donation;
    }
}
