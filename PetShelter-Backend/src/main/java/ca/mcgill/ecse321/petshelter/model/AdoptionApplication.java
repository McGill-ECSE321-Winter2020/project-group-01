package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AdoptionApplication{
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
private boolean isAccepted;

public void setIsAccepted(boolean value) {
    this.isAccepted = value;
}
public boolean isIsAccepted() {
    return this.isAccepted;
}
   private User user;
   
   @ManyToOne(optional=false)
   public User getUser() {
      return this.user;
   }
   
   public void setUser(User user) {
      this.user = user;
   }
   
   }
