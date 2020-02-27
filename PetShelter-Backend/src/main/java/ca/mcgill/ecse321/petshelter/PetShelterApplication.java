package ca.mcgill.ecse321.petshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return "Does it actually deploy automatically with travis to heroku?";
  }

}
