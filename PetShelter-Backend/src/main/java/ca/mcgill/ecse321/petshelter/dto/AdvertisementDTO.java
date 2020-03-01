package ca.mcgill.ecse321.petshelter.dto;

import ca.mcgill.ecse321.petshelter.model.Application;

import java.util.Arrays;
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
        this.adId = adId;
    }
    
    @Override
    public String toString() {
        return "AdvertisementDTO{" +
                "title='" + title + '\'' +
                ", petIds=" + Arrays.toString(petIds) +
                ", description='" + description + '\'' +
                ", isFulfilled=" + isFulfilled +
                ", application=" + application +
                ", adId=" + adId +
                '}';
    }
}