package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class User{
   private String userName;
   
   public void setUserName(String value) {
      this.userName = value;
   }
   
   public String getUserName() {
      return this.userName;
   }
   
   private String password;
   
   public void setPassword(String value) {
      this.password = value;
   }
   
   public String getPassword() {
      return this.password;
   }
   
   private boolean isEmailValidated;
   
   public void setIsEmailValidated(boolean value) {
      this.isEmailValidated = value;
   }
   
   public boolean isIsEmailValidated() {
      return this.isEmailValidated;
   }
   
   private String email;
   
   public void setEmail(String value) {
      this.email = value;
   }
   
   public String getEmail() {
      return this.email;
   }
   
   private String apiToken;
   
   public void setApiToken(String value) {
      this.apiToken = value;
   }
   
   public String getApiToken() {
      return this.apiToken;
   }
   
   private long id;

public void setId(long value) {
    this.id = value;
}
@Id
public long getId() {
    return this.id;
}
   private byte profilePicture;
   
   public void setProfilePicture(byte value) {
      this.profilePicture = value;
   }
   
   public byte getProfilePicture() {
      return this.profilePicture;
   }
   
   /**
    * <pre>
    *           1..1     1..1
    * User ------------------------> StaffOrCustomer
    *           &lt;       staffOrCustomer
    * </pre>
    */
   private StaffOrCustomer staffOrCustomer;
   
   public void setStaffOrCustomer(StaffOrCustomer value) {
      this.staffOrCustomer = value;
   }
   
   public StaffOrCustomer getStaffOrCustomer() {
      return this.staffOrCustomer;
   }
   
   private Set<Pet> pets;
   
   @OneToMany
   public Set<Pet> getPets() {
      return this.pets;
   }
   
   public void setPets(Set<Pet> petss) {
      this.pets = petss;
   }
   
   }
