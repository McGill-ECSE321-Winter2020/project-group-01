package ca.mcgill.ecse321.petshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.ecse321.petshelter.model.Application;

/**
 * @author louis
 *
 */
public interface ApplicationRepository extends JpaRepository<Application, Long>{

}
