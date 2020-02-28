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

@Service
public class DonationService {
    
    @Autowired
    DonationRepository donationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public List<Donation> getAllDonations() {
        return toList(donationRepository.findAll());
    }
    
    @Transactional
    public Donation getDonation(String name, double amount) {
        return donationRepository.findDonationsByUserUserNameAndAmount(name, amount);
    }
    
    @Transactional
    public List<Donation> getAllUserDonations(User name) {
        return toList(donationRepository.findAllByUser(name));
    }
    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
    
    @Transactional
    public Donation createDonation(DonationDTO donationDTO) {
        //condition checks
        if (donationDTO.getAmount() == null) {
            throw new DonationException("Donation can't be null!");
        }
        if (donationDTO.getAmount() < 0.00) {
            throw new DonationException("Donation amount can't be less than 0$");
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
        return donation;
    }
}
