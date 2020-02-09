package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.petshelter.model.Comment;
import javax.persistence.CascadeType;
import ca.mcgill.ecse321.petshelter.model.User;

@Entity
public class Forum {
	private String title;

	@NotNull
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	private Set<Comment> comments;

	@OneToMany
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> commentss) {
		this.comments = commentss;
	}

	private Set<User> subscribers;

	@OneToMany
	public Set<User> getSubscribers() {
		return this.subscribers;
	}

	@Override
	public String toString() {
		return "Forum [title=" + title + ", id=" + id + ", comments=" + comments + ", subscribers=" + subscribers + "]";
	}

	public void setSubscribers(Set<User> subscriberss) {
		this.subscribers = subscriberss;
	}

}
