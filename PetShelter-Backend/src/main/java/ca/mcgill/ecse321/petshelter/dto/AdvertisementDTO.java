package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;

public class AdvertisementDTO {
	
	@NotNull(message = "Description cannot be empty.")
	@NotEmpty(message = "Description cannot be empty.")
	private String description;
	
	@NotNull(message = "Title cannot be empty.")
	@NotEmpty(message = "Title cannot be empty.")
	private String title;
	
	private Set<AdoptionApplication> applications;
	
	private boolean isFulfilled;
	
	public AdvertisementDTO() {
	}
	
	public AdvertisementDTO(@NotNull @NotEmpty String title, @NotNull @NotEmpty String description, boolean isFulfilled, Set<AdoptionApplication> applications) {
		this.description = description;
		this.title = title;
		this.isFulfilled = isFulfilled;
		this.applications = applications;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	
	/**
	 * @return the applications
	 */
	public Set<AdoptionApplication> getApplications() {
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(Set<AdoptionApplication> applications) {
		this.applications = applications;
	}
}
