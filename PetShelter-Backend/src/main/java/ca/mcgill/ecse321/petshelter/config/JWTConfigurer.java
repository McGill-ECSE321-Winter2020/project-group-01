package ca.mcgill.ecse321.petshelter.config;



import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ca.mcgill.ecse321.petshelter.service.JWTTokenFilter;
import ca.mcgill.ecse321.petshelter.service.JWTTokenProvider;
//this class configures the jwt
/**
 * 
 * @author louis
 *
 */
public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JWTTokenProvider jwtTokenProvider;
    public JWTConfigurer(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        JWTTokenFilter customFilter = new JWTTokenFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
