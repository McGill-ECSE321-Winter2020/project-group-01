package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Advertisement {
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

	private boolean isFulfilled;

	public void setIsFulfilled(boolean value) {
		this.isFulfilled = value;
	}

	public boolean isIsFulfilled() {
		return this.isFulfilled;
	}
    
    private Set<AdoptionApplication> adoptionApplication;
    
    @OneToMany(mappedBy = "advertisement")
    public Set<AdoptionApplication> getAdoptionApplication() {
        return this.adoptionApplication;
    }
    
    public void setAdoptionApplication(Set<AdoptionApplication> adoptionApplications) {
        this.adoptionApplication = adoptionApplications;
    }
    
    
    private String title;
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return "Advertisement{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", isFulfilled=" + isFulfilled +
                ", adoptionApplication=" + adoptionApplication +
                ", title='" + title + '\'' +
                '}';
    }
}
