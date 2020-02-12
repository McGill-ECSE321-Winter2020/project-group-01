package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.DonationDTO;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    @GetMapping(value = {"/donation"})
    public List<DonationDTO> getAllDonations() {
        return donationService.getAllUsers().stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public DonationDTO convertToDto(Donation donation) {
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setDate(donation.getDate());
        donationDTO.setTime(donation.getTime());
        donationDTO.setUser(donation.getUser());
        return donationDTO;
    }
    
    @PostMapping(value = {"/donation"})
    public ResponseEntity<?> createDonation(@RequestBody DonationDTO amount) throws IllegalArgumentException {
        Donation donation = donationService.createDonation(amount);
        try {
            amount.setUser(donation.getUser());
            amount.setTime(donation.getTime());
            amount.setDate(donation.getDate());
            amount.setAmount(donation.getAmount());
            return new ResponseEntity<>("Thank you for donation", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}
