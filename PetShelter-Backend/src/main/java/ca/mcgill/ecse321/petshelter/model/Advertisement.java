package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.Set;
import javax.persistence.OneToMany;

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

}
