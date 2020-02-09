package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.ManyToOne;

@Entity
public class Donation {
	private double amount;

	public void setAmount(double value) {
		this.amount = value;
	}

	public double getAmount() {
		return this.amount;
	}

	private long id;

	public void setId(long value) {
		this.id = value;
	}

	@Id
	@GeneratedValue()
	public long getId() {
		return this.id;
	}

	/**
	 * <pre>
	 *           1..1     1..1
	 * Donation ------------------------> Date
	 *           &lt;       date
	 * </pre>
	 */
	private Date date;

	public void setDate(Date value) {
		this.date = value;
	}

	public Date getDate() {
		return this.date;
	}

	/**
	 * <pre>
	 *           1..1     1..1
	 * Donation ------------------------> Time
	 *           &lt;       time
	 * </pre>
	 */
	private Time time;

	public void setTime(Time value) {
		this.time = value;
	}

	public Time getTime() {
		return this.time;
	}

	private User user;

	@ManyToOne
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
