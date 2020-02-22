package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.AdvertisementDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdvertisementService {
    
    @Autowired
    AdvertisementRepository advertisementRepository;
    
    @Transactional
    public List<Advertisement> getAllAdvertisements() {
        return toList(advertisementRepository.findAll());
    }
    
    @Transactional
    public Advertisement getAdvertisement(String title) {
        return advertisementRepository.findAdvertisementByTitle(title);
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
    public Advertisement createAdvertisement(AdvertisementDTO advertisementDTO) {
        //condition checks
        if (advertisementDTO.getDescription() == null) {
            throw new AdvertisementException("Description can't be null!");
        }
        if (advertisementDTO.getTitle() == null) {
        	throw new AdvertisementException("Title can't be null!");
        }
        
        Advertisement advertisement = new Advertisement();
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setIsFulfilled(advertisementDTO.getIsFulfilled());
        advertisement.setTitle(advertisementDTO.getTitle());
        advertisement.setAdoptionApplication(advertisementDTO.getApplications());
        
        advertisementRepository.save(advertisement);
        return advertisement;
    }
}
