package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Forum {
    private String title;
    private long id;
    private Set<Comment> comments;
    private Set<User> subscribers;
    private boolean isLocked;
    private User author;
    
    public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String value) {
        this.title = value;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }
    
    public void setId(long value) {
        this.id = value;
    }
    
    @OneToMany
    public Set<Comment> getComments() {
        return this.comments;
    }
    
    public void setComments(Set<Comment> commentss) {
        this.comments = commentss;
    }
    
    @OneToMany
    public Set<User> getSubscribers() {
        return this.subscribers;
    }
    
    public void setSubscribers(Set<User> subscriberss) {
        this.subscribers = subscriberss;
    }

    @ManyToOne
    public User getAuthor() { return this.author; }

    public void setAuthor(User user) { this.author = user; }

    
    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", comments=" + comments +
                ", subscribers=" + subscribers +
                ", author=" + author +
                '}';
    }
}
