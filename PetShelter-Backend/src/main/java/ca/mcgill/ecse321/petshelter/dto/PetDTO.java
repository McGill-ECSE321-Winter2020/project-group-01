package ca.mcgill.ecse321.petshelter.dto;

import ca.mcgill.ecse321.petshelter.model.Gender;

import java.sql.Date;

public class PetDTO {
	private Date dateOfBirth;
	private String name;
	private String species;
	private String breed;
	private String description;
	private String pictureLink;
	private Long id;
	private Gender gender;
	private Long advertisementId;
	private String userName;

	public PetDTO() {

	}

	public PetDTO(Date date, String name, String spec, String breed, String desc, String pic, Gender gen, Long ad,
			String userName) {
		this.dateOfBirth = date;
		this.name = name;
		this.description = desc;
		this.species = spec;
		this.breed = breed;
		this.pictureLink = pic;
		this.id = null;
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

	public String getPicture() {
		return pictureLink;
	}

	public void setPicture(String picture) {
		this.pictureLink = picture;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (this.id == null) {
			this.id = id;
		}
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Long getAdvertisement() {
		return advertisementId;
	}

	public void setAdvertisement(Long advertisement) {
		this.advertisementId = advertisement;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "PetDTO{" + "dateOfBirth=" + dateOfBirth + ", name='" + name + '\'' + ", species='" + species + '\''
				+ ", breed='" + breed + '\'' + ", description='" + description + '\'' + ", picture=" + pictureLink
				+ ", id=" + id + ", gender=" + gender + ", advertisement=" + advertisementId + ", userName='" + userName
				+ '\'' + '}';
	}
}
