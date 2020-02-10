package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;

@Entity
public class Pet {
	/**
	 * <pre>
	 *           1..1     1..1
	 * Pet ------------------------> Date
	 *           &lt;       dateOfBirth
	 * </pre>
	 */
	private Date dateOfBirth;

	public void setDateOfBirth(Date value) {
		this.dateOfBirth = value;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	private String name;

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}

	private String species;

	public void setSpecies(String value) {
		this.species = value;
	}

	public String getSpecies() {
		return this.species;
	}

	private String breed;

	public void setBreed(String value) {
		this.breed = value;
	}

	public String getBreed() {
		return this.breed;
	}

	private String description;

	public void setDescription(String value) {
		this.description = value;
	}

	public String getDescription() {
		return this.description;
	}

	private byte[] picture;

	public void setPicture(byte[] value) {
		this.picture = value;
	}

	public byte[] getPicture() {
		return this.picture;
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
    
    @Enumerated
    private Gender gender;
    
    public void setGender(Gender value) {
        this.gender = value;
    }
    
    public Gender getGender() {
        return this.gender;
    }
    
    private Advertisement advertisement;
    
    @ManyToOne
    public Advertisement getAdvertisement() {
        return this.advertisement;
    }
    
    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
    
    @Override
    public String toString() {
        return "Pet{" +
                "dateOfBirth=" + dateOfBirth +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + Arrays.toString(picture) +
                ", id=" + id +
                ", gender=" + gender +
                ", advertisement=" + advertisement +
                '}';
    }
}
