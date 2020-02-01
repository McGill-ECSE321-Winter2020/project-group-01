package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import ca.mcgill.ecse321.petshelter.model.java.sql.Date;
import javax.persistence.Id;
import ca.mcgill.ecse321.petshelter.model.java.sql.Time;
import javax.persistence.ManyToOne;

@Entity
public class Comment{
   private Date yearOfPost;

public void setYearOfPost(Date value) {
    this.yearOfPost = value;
}
public Date getYearOfPost() {
    return this.yearOfPost;
}
private Date dayOfPost;

public void setDayOfPost(Date value) {
    this.dayOfPost = value;
}
public Date getDayOfPost() {
    return this.dayOfPost;
}
private long idComment;

public void setIdComment(long value) {
    this.idComment = value;
}
@Id
public long getIdComment() {
    return this.idComment;
}
private Date montOfPost;

public void setMontOfPost(Date value) {
    this.montOfPost = value;
}
public Date getMontOfPost() {
    return this.montOfPost;
}
private String text;

public void setText(String value) {
    this.text = value;
}
public String getText() {
    return this.text;
}
private Time commentTime;

public void setCommentTime(Time value) {
    this.commentTime = value;
}
public Time getCommentTime() {
    return this.commentTime;
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
