package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Getter
	@Setter
	private Date date;

	@ManyToOne
	@Getter
	@Setter
	private User user;

	@Getter
	@Setter
	private Time time;
}
