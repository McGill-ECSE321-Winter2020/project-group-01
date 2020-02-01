package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;

/**
 * @author louis
 *
 */
@Entity
public class Forum {
	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	@Id
	private long id;

	private Set<User> subscribers;

	@OneToMany
	public Set<User> getSubscribers() {
		return this.subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}

	private Set<Comment> comments;

	@OneToMany(mappedBy = "forum", cascade = { CascadeType.ALL })
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

}
