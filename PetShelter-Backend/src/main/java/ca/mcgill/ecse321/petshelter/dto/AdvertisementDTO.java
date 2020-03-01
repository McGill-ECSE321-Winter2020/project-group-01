package ca.mcgill.ecse321.petshelter.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse321.petshelter.dto.ApplicationDTO;
import ca.mcgill.ecse321.petshelter.model.Application;

import java.util.Set;

public class AdvertisementDTO {
    private String title;
    private Long[] petIds;
    private String description;
    private boolean isFulfilled;
    private Set<Application> application;
    private Long adId;
    
    public AdvertisementDTO() {
    
    }
    
    
    public String getTitle() {
        return title;
    }
    
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    public Long[] getPetIds() {
        return petIds;
    }
    
    
    public void setPetIds(Long[] ids) {
        this.petIds = ids;
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
    
    
    public Set<Application> getApplication() {
        return application;
    }
    
    
    public void setApplication(Set<Application> application) {
        this.application = application;
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