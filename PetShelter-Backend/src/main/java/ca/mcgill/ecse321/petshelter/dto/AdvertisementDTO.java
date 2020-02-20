package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;

public class AdvertisementDTO {
	private String title;
	private long petId;
	private String description;
	private boolean isFulfilled;
	private Set<AdoptionApplication> adoptionApplication;
	

	public AdvertisementDTO(String des, boolean isfullfilled, Set<AdoptionApplication> adoptionApplication,
			String title) {
		this.title = title; 
		this.description = des; 
		this.isFulfilled = isfullfilled;
		this.adoptionApplication.addAll(adoptionApplication);
	}

	public Set<AdoptionApplication> getAdoptionApplication() {
		return adoptionApplication;
	}

	public void setAdoptionApplication(Set<AdoptionApplication> adoptionApplication) {
		this.adoptionApplication = adoptionApplication;
	}

	public boolean isFulfilled() {
		return isFulfilled;
	}

	public void setFulfilled(boolean isFulfilled) {
		this.isFulfilled = isFulfilled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPetId() {
		return petId;
	}

	public void setPetId(long petId) {
		this.petId = petId;
	}
}
