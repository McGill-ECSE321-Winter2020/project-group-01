package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToOne;

/**
 * @author louis
 *
 */
@Entity
public class Comment {
	@Getter
	@Setter
	private Date datePosted;

	@Getter
	@Setter
	@Id
	private long id;

	@Getter
	@Setter
	private String text;

	@Getter
	@Setter
	private Time comment;

	private User user;

	@OneToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private Forum forum;

	@ManyToOne(optional = false)
	public Forum getForum() {
		return this.forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

}
