package ca.mcgill.ecse321.petshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entry point of the application.
 * @author louis
 *
 */
@RestController
@SpringBootApplication
@EnableJpaRepositories
public class PetShelterApplication {

	/**
	 * Main method; launches the application.
	 * @param args
	 */
  public static void main(String[] args) {
    SpringApplication.run(PetShelterApplication.class, args);
  }

  /**
   * Just an arbitrary welcome message to the root of the application.
   * @return
   */
  @RequestMapping("/")
  public String greeting(){
    return "Does it actually deploy automatically with travis to heroku?";
  }

}
