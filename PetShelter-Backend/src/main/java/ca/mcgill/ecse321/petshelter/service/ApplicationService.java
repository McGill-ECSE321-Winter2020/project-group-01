package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    
    @Autowired
    ApplicationRepository applicationRepository;
    
    @Autowired
    AdvertisementRepository advertisementRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public List<AdoptionApplication> getAllApplications() {
        return toList(applicationRepository.findAll());
    }
    
    @Transactional
    public AdoptionApplication getApplication(String applicant, Advertisement advertisement) {
        return applicationRepository.findApplicationByUserUserNameAndAdvertisement(applicant, advertisement);
    }
    
    
    @Transactional
    public List<AdoptionApplication> getAllUserApplications(User name) {
        return toList(applicationRepository.findApplicationsByUser(name));
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
    public AdoptionApplication createApplication(ApplicationDTO applicationDTO) {
        //condition checks
        if (applicationDTO.getDescription() == null) {
            throw new ApplicationException("Description can't be null!");
        }
        if (applicationDTO.getUsername() == null) {
        	throw new ApplicationException("Username can't be null!");
        }
        if (applicationDTO.getAdvertisementTitle() == null) {
        	throw new ApplicationException("Advertisement Title can't be null!");
        }
        
        Advertisement advertisement = advertisementRepository.findAdvertisementByTitle(applicationDTO.getAdvertisementTitle());
        User user = userRepository.findUserByUserName(applicationDTO.getUsername());
        AdoptionApplication application = new AdoptionApplication();
        application.setAdvertisement(advertisement);
        application.setDescription(applicationDTO.getDescription());
        application.setIsAccepted(applicationDTO.getIsAccepted());
        application.setUser(user);
        
        applicationRepository.save(application);
        return application;
    }
}
