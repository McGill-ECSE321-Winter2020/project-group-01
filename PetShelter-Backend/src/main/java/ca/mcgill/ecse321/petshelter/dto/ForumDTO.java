package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

public class ForumDTO {
    private String title;
    private long id;
    private Set<CommentDTO> comments;
    private Set<UserDTO> subscribers;
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String value) {
        this.title = value;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long value) {
        this.id = value;
    }
    
    public Set<CommentDTO> getComments() {
        return this.comments;
    }
    
    public void setComments(Set<CommentDTO> commentss) {
        this.comments = commentss;
    }
    
    public Set<UserDTO> getSubscribers() {
        return this.subscribers;
    }
    
    public void setSubscribers(Set<UserDTO> subscriberss) {
        this.subscribers = subscriberss;
    }
    
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", comments=" + comments +
                ", subscribers=" + subscribers +
                '}';
    }
}
