package ca.mcgill.ecse321.petshelter.dto;

import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.passwordvalidator.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {
	
	@NotNull(message = "Email cannot be empty.")
	@Email(message = "The provided email is not a valid email address.")
	@NotEmpty(message = "Email cannot be empty.")
	private String email;
	
	@NotNull(message = "Username cannot be empty.")
	@NotEmpty(message = "Username cannot be empty.")
	@Size(min = 8, message = "Username must have at least 8 characters.")
	private String username;
	
	@NotNull(message = "Password cannot be empty.")
	@NotEmpty(message = "Password cannot be empty.")
	@ValidPassword
	private String password;
	private String token;
	private UserType userType;
	private byte[] picture;
	
	public UserDTO() {
	}
	
	public UserDTO(@NotNull @Email String email, @NotNull String username, String password, UserType userType, byte[] picture) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.picture = picture;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
