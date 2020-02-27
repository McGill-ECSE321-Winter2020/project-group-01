package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

import ca.mcgill.ecse321.petshelter.model.AdoptionApplication;

public class AdvertisementDTO {
	private String title;
	private long[] petIds;
	private String description;
	private boolean isFulfilled;
	private Set<AdoptionApplication> adoptionApplication;
	private Long adId;
	

	public AdvertisementDTO(String title, boolean isfullfilled, long[] petIds, Long adId,  Set<AdoptionApplication> adoptionApplication,
			String des) {
		this.title = title; 
		this.description = des; 
		this.isFulfilled = isfullfilled;
		this.petIds = petIds;
		this.adoptionApplication.addAll(adoptionApplication);
		adId = null;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public long[] getPetIds() {
		return petIds;
	}


	public void setPetIds(long[] petIds) {
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