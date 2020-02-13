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
    
    
    public Donation createDonation(DonationDTO donationDTO) {
        if (donationDTO.getAmount() < 0.00) {
            throw new IllegalArgumentException("Donation cannot be less than 0$");
        }
        System.out.println(donationDTO.toString());
        User user = userRepository.findUserByUserName(donationDTO.getUser());
        //have a form of detection if the user is null. if it is set it to anonymous
//        if (user == null){
//            user.setUserName("Anonymous");
//            user.setEmail("None");
//        }
        Donation donation = new Donation();
        //todo, cant find the username bob
        donation.setUser(user);
        donation.setTime(donationDTO.getTime());
        donation.setDate(donationDTO.getDate());
        donation.setAmount(Math.round(donationDTO.getAmount() * 100.0) / 100.0);//trim it down to 2 decimal points
        donationRepository.save(donation);
        System.out.println(donation.toString());
        return donation;
    }
}
