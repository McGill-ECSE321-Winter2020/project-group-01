package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<AdoptionApplication> getAllAcceptedApplications() {
    	List<AdoptionApplication> allApplications = toList(applicationRepository.findAll());
    	List<AdoptionApplication> acceptedApplications = new ArrayList<AdoptionApplication>();
    	
    	for (AdoptionApplication a : allApplications) {
    		if (a.isIsAccepted()) {
    			acceptedApplications.add(a);
    		}
    	}
    	
    	return acceptedApplications;
    }
    
    @Transactional
    public List<AdoptionApplication> getAllUnacceptedApplications() {
    	List<AdoptionApplication> allApplications = toList(applicationRepository.findAll());
    	List<AdoptionApplication> unacceptedApplications = new ArrayList<AdoptionApplication>();
    	
    	for (AdoptionApplication a : allApplications) {
    		if (! a.isIsAccepted()) {
    			unacceptedApplications.add(a);
    		}
    	}
    	
    	return unacceptedApplications;
    }
    
    @Transactional
    public AdoptionApplication getApplication(String applicant, Advertisement advertisement) {
    	return applicationRepository.findApplicationByUserUserNameAndAdvertisement(applicant, advertisement);
    }
    
    @Transactional
    public AdoptionApplication getApplication(ApplicationDTO applicationDTO) {
        return applicationRepository.findApplicationByUserUserNameAndAdvertisement(applicationDTO.getUsername(), advertisementRepository.findAdvertisementByTitle(applicationDTO.getAdvertisementTitle()));
    }
    
    @Transactional
    public List<AdoptionApplication> getAllUserApplications(User user) {
        return toList(applicationRepository.findApplicationsByUser(user));
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
    
    @Transactional
    public ResponseEntity<?> deleteApplication(ApplicationDTO applicationDTO) {
    	AdoptionApplication application = getApplication(applicationDTO);
    	
    	try {
    		applicationRepository.deleteById(application.getId());
    	} catch (RuntimeException e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Transactional
    public void acceptApplication(ApplicationDTO applicationDTO) {
    	
    	if (applicationDTO == null) {
    		throw new ApplicationException("Application DTO cannot be null!");
    	}
    	
    	AdoptionApplication application = getApplication(applicationDTO);
    	
    	if (application == null) {
    		throw new ApplicationException("Application not found!");
    	}
    	
    	if (application.isIsAccepted() == true) {
    		throw new ApplicationException("Application is already accepted!");
    	}
    	
    	application.setIsAccepted(true);
    	applicationDTO.setIsAccepted(true);
    	applicationRepository.save(application);
    }
    
    @Transactional
    public void rejectApplication(ApplicationDTO applicationDTO) {
    	if (applicationDTO == null) {
    		throw new ApplicationException("Application DTO cannot be null!");
    	}
    	
    	AdoptionApplication application = getApplication(applicationDTO);
    	
    	if (application == null) {
    		throw new ApplicationException("Application not found!");
    	}
    	
    	if (application.isIsAccepted() == false) {
    		throw new ApplicationException("Application is already rejected!");
    	}
    	
    	application.setIsAccepted(false);
    	applicationDTO.setIsAccepted(false);
    	applicationRepository.save(application);
    }
    
    @Transactional
    public void updateApplicationDescription(ApplicationDTO applicationDTO, String newDescription) {
    	if (applicationDTO == null) {
    		throw new ApplicationException("Application DTO cannot be null!");
    	}
    	
    	AdoptionApplication application = getApplication(applicationDTO);
    	
    	if (application == null) {
    		throw new ApplicationException("Application not found!");
    	}
    	
    	if (application.getDescription() == newDescription) {
    		throw new ApplicationException("Description is the same!");
    	}
    	
    	application.setDescription(newDescription);
    }
    
}
