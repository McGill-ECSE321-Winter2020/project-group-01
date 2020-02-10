package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.*;

@Entity
public class AdoptionApplication {
    
    private String description;
    
    public void setDescription(String value) {
        this.description = value;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    private long id;

	public void setId(long value) {
		this.id = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	private boolean isAccepted;

	public void setIsAccepted(boolean value) {
		this.isAccepted = value;
	}

	public boolean isIsAccepted() {
		return this.isAccepted;
	}

	private User user;

	@ManyToOne
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    private Advertisement advertisement;
    
    @ManyToOne(optional = false)
    public Advertisement getAdvertisement() {
        return this.advertisement;
    }
    
    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
    
    @Override
    public String toString() {
        return "AdoptionApplication{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", isAccepted=" + isAccepted +
                ", user=" + user +
                ", advertisement=" + advertisement +
                '}';
    }
}
