package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.petshelter.service.extrafeatures.ValidPassword;

/**
 * @author louis
 *
 */
public class PasswordChangeDTO {
	@NotNull
	@NotEmpty
	private String oldPassword;
	
	@NotNull
	@NotEmpty
	@ValidPassword
	private String newPassword;
	
	@NotNull
	@NotEmpty
	private String userName;

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}	
}

