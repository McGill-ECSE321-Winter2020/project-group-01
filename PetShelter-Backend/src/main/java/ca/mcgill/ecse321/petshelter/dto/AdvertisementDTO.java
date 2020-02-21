package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AdvertisementDTO {
	
	@NotNull(message = "Description cannot be empty.")
	@NotEmpty(message = "Description cannot be empty.")
	private String description;
	
	private boolean isFulfilled;
	
	public AdvertisementDTO() {
	}
	
	public AdvertisementDTO(@NotNull @NotEmpty String description, boolean isFulfilled) {
		this.description = description;
		this.isFulfilled = isFulfilled;
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
	 * @return the isFulfilled
	 */
	public boolean getIsFulfilled() {
		return isFulfilled;
	}

	/**
	 * @param isFulfilled the isFulfilled to set
	 */
	public void setIsFulfilled(boolean isFulfilled) {
		this.isFulfilled = isFulfilled;
	}
}
