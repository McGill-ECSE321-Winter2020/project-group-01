package ca.mcgill.ecse321.petshelter.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//taken from Baeldung's tutorials, not 100% sure how it works
/**
 * This class ensures that the JWTToken that is generated is valid.
 * @author louis
 *
 */
public class JWTTokenFilter extends GenericFilterBean {
    private JWTTokenProvider jwtTokenProvider;
    public JWTTokenFilter(JWTTokenProvider j) {jwtTokenProvider=j;}
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
			    Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
			    SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        filterChain.doFilter(req, res);
    }
}