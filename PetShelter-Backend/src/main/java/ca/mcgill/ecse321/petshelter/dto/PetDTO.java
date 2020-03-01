package ca.mcgill.ecse321.petshelter.dto;

import java.sql.Date;

import ca.mcgill.ecse321.petshelter.model.Gender;


public class PetDTO {
	private Date dateOfBirth;
	private String name;
	private String species;
	private String breed;
	private String description;
	private byte[] picture;
	public Long petId;
    private Gender gender;
    private Long advertisementId;
    private String userName;
	

    public PetDTO() {
        
    }
	public PetDTO(Date date, String name, String spec, String breed, String desc, byte [] pic, Gender gen, Long ad, String userName) {
		this.dateOfBirth = date;
		this.name = name;
		this.description = desc;
		this.species = spec;
		this.breed = breed;
		this.picture = pic;
		this.petId = null;
		this.gender = gen;
		this.advertisementId = ad;
		this.userName = userName;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSpecies() {
		return species;
	}


	public void setSpecies(String species) {
		this.species = species;
	}


	public String getBreed() {
		return breed;
	}


	public void setBreed(String breed) {
		this.breed = breed;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public byte[] getPicture() {
		return picture;
	}


	public void setPicture(byte[] picture) {
		this.picture = picture;
	}


	public Long getId() {
		return petId;
	}

	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public long getAdvertisement() {
		return advertisementId;
	}


	public void setAdvertisement(long advertisement) {
		this.advertisementId = advertisement;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void SetUserName(String userName) {
		this.userName = userName;
	}
	
	
}
