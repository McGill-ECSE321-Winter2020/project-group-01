package ca.mcgill.ecse321.petshelter.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;

public class AdvertisementDTO {
	private String title;
	private List<Long> petIds;
	private String description;
	private boolean isFulfilled;
	private Set<AdoptionApplication> adoptionApplication = new HashSet<AdoptionApplication>();
	private Long adId;
	

	public AdvertisementDTO(String title, boolean isfullfilled, List<Long> petIds, Set<AdoptionApplication> adoptionApplication,
			String des) {
		this.title = title; 
		this.description = des; 
		this.isFulfilled = isfullfilled;
		this.petIds = petIds;
		this.adoptionApplication.addAll(adoptionApplication);
		this.adId = null;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public List<Long> getPetIds() {
		return petIds;
	}


	public void setPetIds(List<Long> petIds) {
		this.petIds = petIds;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public boolean isFulfilled() {
		return isFulfilled;
	}


	public void setFulfilled(boolean isFulfilled) {
		this.isFulfilled = isFulfilled;
	}


	public Set<AdoptionApplication> getAdoptionApplication() {
		return adoptionApplication;
	}


	public void setAdoptionApplication(Set<AdoptionApplication> adoptionApplication) {
		this.adoptionApplication = adoptionApplication;
	}


	public Long getAdId() {
		return adId;
	}


	public void setAdId(Long adId) {
		if (this.adId == null) {
		this.adId = adId;
		}
	}

}