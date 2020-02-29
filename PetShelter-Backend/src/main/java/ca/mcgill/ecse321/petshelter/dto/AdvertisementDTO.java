package ca.mcgill.ecse321.petshelter.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;

public class AdvertisementDTO {
	private String title;
	private List<Long> petIds;
	private String description;
	private boolean isFulfilled;
	private Set<ApplicationDTO> applications = new HashSet<ApplicationDTO>();
	public Long adId;
	

	public AdvertisementDTO(String title, boolean isfullfilled, List<Long> petIds, Set<ApplicationDTO> applications,
			String des) {
		this.title = title; 
		this.description = des; 
		this.isFulfilled = isfullfilled;
		this.petIds = petIds;
		this.applications.addAll(applications);
		this.adId = null;
	}


	public AdvertisementDTO() {

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


	public Set<ApplicationDTO> getApplicationDTO() {
		return applications;
	}


	public void setApplicationDTO(Set<ApplicationDTO> applications) {
		this.applications = applications;
	}


	public Long getAdId() {
		return adId;
	}
}