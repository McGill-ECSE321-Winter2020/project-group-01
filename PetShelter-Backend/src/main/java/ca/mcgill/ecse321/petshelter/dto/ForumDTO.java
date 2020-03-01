package ca.mcgill.ecse321.petshelter.dto;

import java.util.Set;

public class ForumDTO {
    private String title;
    private long id;
    private Set<CommentDTO> comments;
    private Set<UserDTO> subscribers;
    private UserDTO author;
    private boolean isLocked;
    
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
    
    public UserDTO getAuthor() {
        return author;
    }
    
    public void setAuthor(UserDTO author) {
        this.author = author;
    }
    
    public String toString() {
        return "ForumDTO{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", comments=" + comments +
                ", subscribers=" + subscribers +
                ". author=" + author +
                '}';
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
