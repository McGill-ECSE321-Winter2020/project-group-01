package ca.mcgill.ecse321.petshelter.dto;

import java.sql.Date;
import java.util.Set;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Gender;


public class PetDTO {
	private Date dateOfBirth;
	private String name;
	private String species;
	private String breed;
	private String description;
	private byte[] picture;
	private long id;
    private Gender gender;
    private AdvertisementDTO advertisement;
	

	public PetDTO(Date date, String name, String spec, String breed, String desc, byte [] pic, long id, Gender gen, AdvertisementDTO ad) {
		this.dateOfBirth = date;
		this.name = name;
		this.species = spec;
		this.breed = breed;
		this.picture = pic;
		this.id = id;
		this.gender = gen;
		this.advertisement = ad;
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


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public AdvertisementDTO getAdvertisementDTO() {
		return advertisement;
	}


	public void setAdvertisementDTO(AdvertisementDTO advertisement) {
		this.advertisement = advertisement;
	}
}
