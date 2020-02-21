package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.sql.Time;

public class ApplicationDTO {
	
	@NotNull(message = "Description cannot be empty.")
	@NotEmpty(message = "Description cannot be empty.")
	private String description;
	
	private boolean isAccepted;
	
	public ApplicationDTO() {
	}
	
	public ApplicationDTO(@NotNull @NotEmpty String description, boolean isAccepted) {
		this.description = description;
		this.isAccepted = isAccepted;
	}
	
	/**
	 * @return the description
	 */
	public String getText() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setText(String description) {
		this.description = description;
	}

	/**
	 * @return the isAccepted
	 */
	public boolean getIsAccepted() {
		return isAccepted;
	}

	/**
	 * @param isAccepted the isAccepted to set
	 */
	public void setIsFulfilled(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
}
