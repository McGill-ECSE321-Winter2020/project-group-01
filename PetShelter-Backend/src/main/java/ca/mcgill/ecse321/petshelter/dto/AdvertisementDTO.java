package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

public class AdvertisementDTO {
    private String title;
    private PetDTO pet;
    private String description;
    private boolean isFulfilled;
    private Set<ApplicationDTO> application;
    private Long adId;
    
    public AdvertisementDTO() {
    
    }
    
    
    public String getTitle() {
        return title;
    }
    
    
    public void setTitle(String title) {
        this.title = title;
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


    public Set<ApplicationDTO> getApplication() {
        return application;
    }


    public void setApplication(Set<ApplicationDTO> application) {
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
                ", pet=" + pet +
                ", description='" + description + '\'' +
                ", isFulfilled=" + isFulfilled +
                ", application=" + application +
                ", adId=" + adId +
                '}';
    }

    public PetDTO getPet() {
        return pet;
    }

    public void setPet(PetDTO pet) {
        this.pet = pet;
    }
}