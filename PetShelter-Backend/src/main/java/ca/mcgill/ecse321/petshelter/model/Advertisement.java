package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Advertisement{
   private String desciption;

public void setDesciption(String value) {
    this.desciption = value;
}
public String getDesciption() {
    return this.desciption;
}
private long idAd;

public void setIdAd(long value) {
    this.idAd = value;
}
@Id
public long getIdAd() {
    return this.idAd;
}
private boolean isCompleted;

public void setIsCompleted(boolean value) {
    this.isCompleted = value;
}
public boolean isIsCompleted() {
    return this.isCompleted;
}
}
