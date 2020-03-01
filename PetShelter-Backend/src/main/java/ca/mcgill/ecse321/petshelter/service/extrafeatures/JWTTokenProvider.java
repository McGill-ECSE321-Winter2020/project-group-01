package ca.mcgill.ecse321.petshelter.service.extrafeatures;


import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


//taken from Baeldung's tutorials, not 100% sure how it works
/**
 * This class generates the JWTTOkens used to confirm a user's account.
 * @author louis
 *
 */
@Component
public class JWTTokenProvider {
	private String secretKey = "secret";
	
	//a token is valid for 3 days
	private long validityInMilliseconds = 3600000*24*3;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private MessageSource ms;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// creates a token
	public String createToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	// dont know what this does but it looks important
	public Authentication getAuthentication(String token) {
		User userDetails = this.userRepo.findUserByUserName(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", null);
	}

	//extracts the username from the token
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	// dont know what this does but it looks important
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	//check that the validation was made before the expiry time
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return claims.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			throw new RuntimeException(ms.getMessage("invalidToken", null, LocaleContextHolder.getLocale()));
		}
	}
}
