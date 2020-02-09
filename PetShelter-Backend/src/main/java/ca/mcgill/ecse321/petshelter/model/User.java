package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import ca.mcgill.ecse321.petshelter.model.Gender;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	private String userName;

	public void setUserName(String value) {
		this.userName = value;
	}

	public String getUserName() {
		return this.userName;
	}

	private String password;

	public void setPassword(String value) {
		this.password = value;
	}

	public String getPassword() {
		return this.password;
	}

	private boolean isEmailValidated;

	public void setIsEmailValidated(boolean value) {
		this.isEmailValidated = value;
	}

	public boolean isIsEmailValidated() {
		return this.isEmailValidated;
	}

	private String email;

	public void setEmail(String value) {
		this.email = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	private long id;

	public void setId(long value) {
		this.id = value;
	}

	public String getEmail() {
		return this.email;
	}

	private String apiToken;

	public void setApiToken(String value) {
		this.apiToken = value;
	}

	public String getApiToken() {
		return this.apiToken;
	}

	@Enumerated
	private Gender gender;

	public void setGender(Gender value) {
		this.gender = value;
	}

	public Gender getGender() {
		return this.gender;
	}

	private Set<Pet> pets;

	@OneToMany
	public Set<Pet> getPets() {
		return this.pets;
	}

	public void setPets(Set<Pet> petss) {
		this.pets = petss;
	}

	private Set<AdoptionApplication> applications;

	@OneToMany(mappedBy = "user")
	public Set<AdoptionApplication> getApplications() {
		return this.applications;
	}

	public void setApplications(Set<AdoptionApplication> applicationss) {
		this.applications = applicationss;
	}
	
	private byte[] picture;

	public void setPicture(byte[] value) {
		this.picture = value;
	}

	public byte[] getPicture() {
		return this.picture;
	}
	
	@Enumerated
	private UserType userType;

	public void setUserType(UserType value) {
		this.userType = value;
	}

	public UserType getUserType() {
		return this.userType;
	}

}
