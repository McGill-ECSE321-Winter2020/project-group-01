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
    
    private boolean isFulfilled;
    private Set<Application> application;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }
    
    public boolean isIsFulfilled() {
        return this.isFulfilled;
    }
    
    public void setIsFulfilled(boolean value) {
        this.isFulfilled = value;
    }
    
    @OneToMany(mappedBy = "advertisement")
    public Set<Application> getApplication() {
        return this.application;
    }
    
    public void setApplication(Set<Application> applications) {
        this.application = applications;
    }

    private String title;
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    private Pet pet;

    @OneToOne
    @PrimaryKeyJoinColumn
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
    
    @Override
    public String toString() {
        return "Advertisement{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", isFulfilled=" + isFulfilled +
                ", adoptionApplication=" + application +
                ", title='" + title + '\'' +
                ", pet=" + pet +
                '}';
    }

}
