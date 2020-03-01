package ca.mcgill.ecse321.petshelter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This config class allows all types of requests to be made to the back-end.
 * Without it, delete, put, patch, etc are denied access because of spring
 * security.
 * 
 * @author louis
 *
 */
@Configuration
public class Cors implements WebMvcConfigurer {

	private String url = "https://petshelter-backend.herokuapp.com/";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins(url);
	}
}
