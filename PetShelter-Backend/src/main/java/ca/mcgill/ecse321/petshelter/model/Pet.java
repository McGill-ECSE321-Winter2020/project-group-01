package ca.mcgill.ecse321.petshelter.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author louis
 *
 */
@Entity
public class Pet {
	@Getter
	@Setter
	private Date dateOfBirth;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String species;

	@Getter
	@Setter
	private String breed;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private byte[] picture;

	@Getter
	@Setter
	@Id
	private long id;

	@Getter
	@Setter
	@Enumerated
	private Gender gender;

	@ManyToOne
	@Getter
	@Setter
	private Advertisement advertisement;

}
