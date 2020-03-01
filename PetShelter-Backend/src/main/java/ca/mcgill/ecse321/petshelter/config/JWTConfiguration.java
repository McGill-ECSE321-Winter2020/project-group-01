package ca.mcgill.ecse321.petshelter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ca.mcgill.ecse321.petshelter.service.extrafeatures.JWTTokenProvider;

/**
 * Class that applies JWT to the application, and allows all routes to be
 * accessed.
 * 
 * @author louis
 *
 */
@Configuration
public class JWTConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	JWTTokenProvider jwtTokenProvider;

	/**
	 * Customizes the authentication manager; taken from baeldung's tutorials.
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Sets up token generation.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.apply(new JWTConfigurer(jwtTokenProvider));
	}

	/**
	 * Allows all urls to be accessed, bypassing spring security.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**");

	}
}
