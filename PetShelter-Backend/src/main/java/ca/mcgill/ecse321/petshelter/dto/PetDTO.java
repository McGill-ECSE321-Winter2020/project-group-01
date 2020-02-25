package ca.mcgill.ecse321.petshelter.dto;

import ca.mcgill.ecse321.petshelter.model.Gender;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Date;

public class PetDTO {
	
	@NotNull(message = "Name cannot be empty.")
	@NotEmpty(message = "Name cannot be empty.")
	private String name;
	
	@NotNull(message = "Species cannot be empty.")
	@NotEmpty(message = "Species cannot be empty.")
	private String species;
	
	@NotNull(message = "Breed cannot be empty.")
	@NotEmpty(message = "Breed cannot be empty.")
	private String breed;
	
	@NotNull(message = "Description cannot be empty.")
	@NotEmpty(message = "Description cannot be empty.")
	private String description;
	
	private Date dateOfBirth;
	private Gender gender;
	private byte[] picture;
	
	private String advertisementTitle; // Can be null
	
	public PetDTO() {
	}
	
	public PetDTO(@NotNull @NotEmpty String name, @NotNull @NotEmpty String species, @NotNull @NotEmpty String breed, @NotNull @NotEmpty String description, String advertisementTitle, Date dateOfBrith, Gender gender, byte[] picture) {
		this.name = name;
		this.species = species;
		this.breed = breed;
		this.description = description;
		this.advertisementTitle = advertisementTitle;
		this.dateOfBirth = dateOfBrith;
		this.gender = gender;
		this.picture = picture;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * @param species the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * @return the breed
	 */
	public String getBreed() {
		return breed;
	}

	/**
	 * @param breed the breed to set
	 */
	public void setBreed(String breed) {
		this.breed = breed;
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
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * @return the dateOfBrith
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	/**
	 * @param picture the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
