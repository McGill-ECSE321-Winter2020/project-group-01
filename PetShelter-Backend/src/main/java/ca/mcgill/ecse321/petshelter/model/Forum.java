package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class Forum{
   private String title;

public void setTitle(String value) {
    this.title = value;
}
public String getTitle() {
    return this.title;
}
private long idForum;

public void setIdForum(long value) {
    this.idForum = value;
}
@Id
public long getIdForum() {
    return this.idForum;
}
   private Set<User> users;
   
   @OneToMany
   public Set<User> getUsers() {
      return this.users;
   }
   
   public void setUsers(Set<User> userss) {
      this.users = userss;
   }
   
   }
