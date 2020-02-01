package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import Gender;
import javax.persistence.OneToOne;

@Entity
public class Pet{
   private int age;

public void setAge(int value) {
    this.age = value;
}
public int getAge() {
    return this.age;
}
private String name;

public void setName(String value) {
    this.name = value;
}
public String getName() {
    return this.name;
}
private String animalKind;

public void setAnimalKind(String value) {
    this.animalKind = value;
}
public String getAnimalKind() {
    return this.animalKind;
}
private String race;

public void setRace(String value) {
    this.race = value;
}
public String getRace() {
    return this.race;
}
private String description;

public void setDescription(String value) {
    this.description = value;
}
public String getDescription() {
    return this.description;
}
private byte pictures;

public void setPictures(byte value) {
    this.pictures = value;
}
public byte getPictures() {
    return this.pictures;
}
private long idPet;

public void setIdPet(long value) {
    this.idPet = value;
}
@Id
public long getIdPet() {
    return this.idPet;
}
private Gender gender;

public void setGender(Gender value) {
    this.gender = value;
}
public Gender getGender() {
    return this.gender;
}
   private Advertisement advertisement;
   
   @OneToOne(optional=false)
   public Advertisement getAdvertisement() {
      return this.advertisement;
   }
   
   public void setAdvertisement(Advertisement advertisement) {
      this.advertisement = advertisement;
   }
   
   }
