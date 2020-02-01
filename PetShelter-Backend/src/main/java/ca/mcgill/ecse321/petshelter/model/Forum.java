package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

/**
 * @author louis
 *
 */
@Entity
public class Forum{
   private String title;

public void setTitle(String value) {
    this.title = value;
}
public String getTitle() {
    return this.title;
}
private long id;

public void setId(long value) {
    this.id = value;
}
@Id
public long getId() {
    return this.id;
}
   private Set<User> subscribers;
   
   @OneToMany
   public Set<User> getSubscribers() {
      return this.subscribers;
   }
   
   public void setSubscribers(Set<User> subscriberss) {
      this.subscribers = subscriberss;
   }
   
   private Set<Comments> comments;
   
   @OneToMany(mappedBy="forum" , cascade={CascadeType.ALL})
   public Set<Comments> getComments() {
      return this.comments;
   }
   
   public void setComments(Set<Comments> commentss) {
      this.comments = commentss;
   }
   
   }
