package ca.mcgill.ecse321.petshelter.model;
import javax.persistence.OneToOne;

import javax.persistence.Entity;
import javax.persistence.Id;
import StaffOrCustomer;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class User {
private Comments comments;
   
   @OneToOne(optional=false)
   public Comments getComments() {
      return this.comments;
   }
   
   public void setComments(Comments comments) {
      this.comments = comments;
   }
   
	private String name;

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
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

	private long idUser;

	public void setIdUser(long value) {
		this.idUser = value;
	}

	@Id
	public long getIdUser() {
		return this.idUser;
	}

	private byte picture;

	public void setPicture(byte value) {
		this.picture = value;
	}

	public byte getPicture() {
		return this.picture;
	}

	private StaffOrCustomer staffOrCustomer;

	public void setStaffOrCustomer(StaffOrCustomer value) {
		this.staffOrCustomer = value;
	}

	public StaffOrCustomer getStaffOrCustomer() {
		return this.staffOrCustomer;
	}

	private Set<Pet> pets;

	@OneToMany
	public Set<Pet> getPets() {
		return this.pets;
	}

	public void setPets(Set<Pet> petss) {
		this.pets = petss;
	}

}
