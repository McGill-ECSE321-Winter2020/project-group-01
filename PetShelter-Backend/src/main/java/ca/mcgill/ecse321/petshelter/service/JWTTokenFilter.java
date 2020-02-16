package ca.mcgill.ecse321.petshelter.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
			e.printStackTrace();
		}
        filterChain.doFilter(req, res);
    }
}