package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.AdvertisementRepository;
import ca.mcgill.ecse321.petshelter.repository.ApplicationRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    
    @Autowired
    ApplicationRepository applicationRepository;
    
    @Autowired
    AdvertisementRepository advertisementRepository;
    
    @Autowired
    UserRepository userRepository;
    
    /**
     * @return list of all applications
     */
    @Transactional
    public List<ApplicationDTO> getAllApplications() {
        return toList(applicationRepository.findAll()).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    /**
     * @param applicationId the applicationId
     * @return applicationDTO that matches the application found
     */
    @Transactional
    public ApplicationDTO getApplicationById(Long applicationId) {
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
        	return convertToDto(application.get());
        } else {
        	throw new ApplicationException("Application not found");
        }
    }
    
    /**
     * @param name user object
     * @return all the applications that match that user
     */
    @Transactional
    public List<ApplicationDTO> getAllUserApplications(String name) {
        return toList(applicationRepository.findApplicationsByUserUserName(name)).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    /**
     * @param advertisement Advertisement entity object.
     * @return all the applications that match that advertisement.
     */
    @Transactional
    public List<ApplicationDTO> getAllAdvertisementApplications(Advertisement advertisement) {
        return toList(applicationRepository.findApplicationsByAdvertisement(advertisement)).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    

    
    /**
     * @return all the accepted applications
     */
    @Transactional
    public List<ApplicationDTO> getAllAcceptedApplications() {
        List<Application> allApplications = toList(applicationRepository.findAll());
        List<Application> acceptedApplications = new ArrayList<>();
        for (Application a : allApplications) {
            if (a.isIsAccepted()) {
                acceptedApplications.add(a);
            }
        }
    
        return toList(acceptedApplications.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    /**
     * @return all the applications that were not accepted
     */
    @Transactional
    public List<ApplicationDTO> getAllUnacceptedApplications() {
        List<Application> allApplications = toList(applicationRepository.findAll());
        System.out.println(allApplications);
        List<Application> unacceptedApplications = new ArrayList<>();
    
        for (Application a : allApplications) {
            if (!a.isIsAccepted()) {
                unacceptedApplications.add(a);
            }
        }
    
        return toList(unacceptedApplications.stream().map(this::convertToDto).collect(Collectors.toList()));
    }
    
    /**
     * Creates an adoption and saves it in the database
     *
     * @param applicationDTO application DTO from controller
     * @return adoption application object
     */
    @Transactional
    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
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
        Advertisement advertisement = advertisementRepository.findAdvertisementById(applicationDTO.getAdId());
        if (advertisement == null) {
            throw new ApplicationException("Advertisement can't be null!");
        }
        User user = userRepository.findUserByUserName(applicationDTO.getUsername());
        Application application = new Application();
        application.setAdvertisement(advertisement);
        application.setDescription(applicationDTO.getDescription());
        application.setIsAccepted(applicationDTO.getIsAccepted());
    
        Set<Application> userApplications = user.getApplications();
        Set<Application> adApplications = advertisement.getApplication();
        adApplications.add(application);
        advertisement.setApplication(adApplications);
        application.setUser(user);
        userApplications.add(application);
        user.setApplications(userApplications);
        applicationRepository.save(application);
        userRepository.save(user);
        advertisementRepository.save(advertisement);
        return convertToDto(application);
    }    
    
    //todo check if they are the owner
    
    /**
     * @param appId application id
     */
    @Transactional
    public void deleteApplication(Long appId) {
        Optional<Application> app = applicationRepository.findById(appId);
        if (app.isPresent()) {
            applicationRepository.deleteById(appId);
        } else {
        	throw new ApplicationException("Application does not exist");
        }
    }
    
    /**
     * Converts Application to ApplicationDTO
     *
     * @param application application object
     * @return application DTO
     */
    public ApplicationDTO convertToDto(Application application) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setDescription(application.getDescription());
        applicationDTO.setUsername(application.getUser().getUserName());
        applicationDTO.setAdvertisementTitle(application.getAdvertisement().getTitle());
        applicationDTO.setIsAccepted(application.isIsAccepted());
        applicationDTO.setAdId(application.getAdvertisement().getId());
        applicationDTO.setAppId(application.getId());
        return applicationDTO;
    }

    /**
     * Converts ApplicationDTO to Application
     *
     * @param applicationDTO application DTO
     * @return application entity object./
     */
    public Application convertToEntity(ApplicationDTO applicationDTO) {
        Application application = new Application();
        application.setDescription(applicationDTO.getDescription());
        User user = userRepository.findUserByUserName(applicationDTO.getUsername());
        if (user != null) {
            application.setUser(user);
        } else {
            throw new ApplicationException("User does not exist.");
        }
        Optional<Advertisement> advertisement = advertisementRepository.findById(applicationDTO.getAdId());
        if (advertisement.isPresent()) {
            application.setAdvertisement(advertisement.get());
        } else {
            throw new ApplicationException(("Advertisement does not exist."));
        }
        application.setIsAccepted(applicationDTO.getIsAccepted());
        application.setId(applicationDTO.getAppId());
        return application;
    }
    
    /**
     * Updates the application description
     * @param applicationId the application id
     * @param newDescription the new description
     * @return applicationDTO that matches the new application
     */
    @Transactional
    public ApplicationDTO updateApplication(long applicationId, String newDescription) {
    	Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            Application newApplication = application.get();
            if (newApplication.getDescription().equals(newDescription)) {
                throw new ApplicationException("Description is the same!");
            }
        
            newApplication.setDescription(newDescription);
            applicationRepository.save(newApplication);
            return convertToDto(newApplication);
        } else {
            throw new ApplicationException("No such application.");
        }
    }
    

    

    
    //From tutorial
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
