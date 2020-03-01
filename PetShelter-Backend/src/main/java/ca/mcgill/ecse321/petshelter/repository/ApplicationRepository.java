package ca.mcgill.ecse321.petshelter.repository;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Application;
import ca.mcgill.ecse321.petshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author louis
 */
//REST endpoint specification
@RepositoryRestResource(collectionResourceRel = "application_data", path = "application_data")
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Application findApplicationByUserUserNameAndAdvertisement(String applicant, Advertisement advertisement);
    
    List<Application> findApplicationsByUser(User user);
    
    List<Application> findApplicationsByUserUserName(String username);
    
    void deleteAdoptionApplicationsByUserUserName(String username);
}
