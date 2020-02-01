package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author louis
 *
 */
@Entity
public class Donation {
	@Getter
	@Setter
	private double amount;

	@Getter
	@Setter
	@Id
	private long id;

	@Getter
	@Setter
	private Date date;

	private User user;

	@ManyToOne
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Getter
	@Setter
	private Time time;
}
