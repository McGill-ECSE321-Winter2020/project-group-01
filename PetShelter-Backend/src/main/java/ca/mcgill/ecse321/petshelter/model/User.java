package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author louis
 *
 */
@Entity
@Table(name="users")
public class User {
	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	private boolean isEmailValidated;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private String apiToken;

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Getter
	@Setter
	private byte[] picture;

	@Enumerated
	@Getter
	@Setter
	private UserType userType;

	@OneToMany
	@Getter
	@Setter
	private Set<Pet> pets;
	
	@ManyToMany
	@Getter
	@Setter
	private Set<Application> applications;

}
