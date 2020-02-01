package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author louis
 *
 */
@Entity
public class Pet {
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

	private byte picture;

	public void setPicture(byte value) {
		this.picture = value;
	}

	public byte getPicture() {
		return this.picture;
	}

	private long id;

	public void setId(long value) {
		this.id = value;
	}

	@Id
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

}
