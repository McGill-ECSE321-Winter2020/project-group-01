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
     * @param applicant     targeted applicant username
     * @param advertisement advertisement
     * @return Adoption application that matches the 2 parameter
     */
    @Transactional
    public ApplicationDTO getApplication(String applicant, Advertisement advertisement) {
        return convertToDto(applicationRepository.findApplicationByUserUserNameAndAdvertisement(applicant, advertisement));
    }
    
    /**
     * @param name user object
     * @return all the applications that matches that user
     */
    @Transactional
    public List<ApplicationDTO> getAllUserApplications(User name) {
        return toList(applicationRepository.findApplicationsByUser(name)).stream().map(this::convertToDto).collect(Collectors.toList());
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
        
        return applicationDTO;
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
