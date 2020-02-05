/*package ca.mcgill.ecse321.petshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetShelterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetShelterApplication.class, args);
	}

}*/
package ca.mcgill.ecse321.petshelter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author louis
 *
 */
@RestController
@SpringBootApplication
@EnableJpaRepositories
public class PetShelterApplication {

  public static void main(String[] args) {
    SpringApplication.run(PetShelterApplication.class, args);
  }

  @RequestMapping("/")
  public String greeting(){
    return "This is a test";
  }

}
