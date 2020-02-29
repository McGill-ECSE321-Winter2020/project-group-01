package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApplicationDTO {
	
	@NotNull(message = "Description cannot be empty.")
	@NotEmpty(message = "Description cannot be empty.")
	private String description;
	
	@NotNull(message = "AdvertisementTitle cannot be empty.")
	@NotEmpty(message = "AdvertisementTitle cannot be empty.")
	private String advertisementTitle;
	
	@NotNull(message = "Username cannot be empty.")
	@NotEmpty(message = "Username cannot be empty.")
	private String username;
	
	private boolean isAccepted;
	
	@NotNull(message = "Advertisement cannot be empty.")
	private Long adId;
	
    public Long appId;
		
	public ApplicationDTO() {
	}
	
	public ApplicationDTO(@NotNull @NotEmpty String advertisementTitle,@NotNull @NotEmpty String description, @NotNull @NotEmpty String username, boolean isAccepted, Long adId) {
		this.advertisementTitle = advertisementTitle;
		this.description = description;
		this.username = username;
		this.isAccepted = isAccepted;
		this.setAdId(adId);
		this.appId = null;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the advertisementTitle
	 */
	public String getAdvertisementTitle() {
		return advertisementTitle;
	}
	
	/**
	 * @param advertisementTitle the advertisementTitle to set
	 */
	public void setAdvertisementTitle(String advertisementTitle) {
		this.advertisementTitle = advertisementTitle;
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
	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getAppId() {
        return appId;
    }
}
