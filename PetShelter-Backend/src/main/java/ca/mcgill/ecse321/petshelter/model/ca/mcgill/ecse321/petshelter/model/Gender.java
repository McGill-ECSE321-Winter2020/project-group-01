package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;

@Entity
public enum Gender{
   private String male;

public void setMale(String value) {
    this.male = value;
}
public String getMale() {
    return this.male;
}
private String female;

public void setFemale(String value) {
    this.female = value;
}
public String getFemale() {
    return this.female;
}
}
