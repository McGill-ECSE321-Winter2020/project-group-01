package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Getter
	@Setter
	private String text;

	@Getter
	@Setter
	private Time comment;

	@OneToOne(optional = false)
	@Getter
	@Setter
	private User user;

	@ManyToOne(optional = false)
	@Getter
	@Setter
	private Forum forum;

}
