package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Advertisement{
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
}
