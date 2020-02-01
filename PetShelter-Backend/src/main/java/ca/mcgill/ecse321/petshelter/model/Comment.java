package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;

/**
 * @author louis
 *
 */
@Entity
public class Comment{
   private Date datePosted;

public void setDatePosted(Date value) {
    this.datePosted = value;
}
public Date getDatePosted() {
    return this.datePosted;
}
private long id;

public void setId(long value) {
    this.id = value;
}
@Id
public long getId() {
    return this.id;
}
private String text;

public void setText(String value) {
    this.text = value;
}
public String getText() {
    return this.text;
}
private Time comment;

public void setComment(Time value) {
    this.comment = value;
}
public Time getComment() {
    return this.comment;
}
   private User user;
   
   @OneToOne(optional=false)
   public User getUser() {
      return this.user;
   }
   
   public void setUser(User user) {
      this.user = user;
   }
   
   private Forum forum;
   
   @ManyToOne(optional=false)
   public Forum getForum() {
      return this.forum;
   }
   
   public void setForum(Forum forum) {
      this.forum = forum;
   }
   
   }
