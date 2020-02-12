package ca.mcgill.ecse321.petshelter.service;

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
    
    
    public Donation createDonation(double amount) {
        if (amount < 0.00) {
            throw new IllegalArgumentException("Donation cannot be less than 0$");
        }
        Donation donation = new Donation();
        donation.setAmount(amount);
        donationRepository.save(donation);
        return donation;
    }
}
