package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * @author louis
 *
 */
@Entity
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
	private long idUser;

	@Getter
	@Setter
	private byte[] picture;

	@Enumerated
	@Getter
	@Setter
	private UserType userType;

	private Set<Pet> pets;

	@OneToMany
	public Set<Pet> getPets() {
		return this.pets;
	}

	public void setPets(Set<Pet> pets) {
		this.pets = pets;
	}

}
