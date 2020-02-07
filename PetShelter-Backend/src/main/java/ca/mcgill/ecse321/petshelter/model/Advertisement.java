package ca.mcgill.ecse321.petshelter.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author louis
 *
 */
@Entity
@Data
@Table(name = "Advertisements")
public class Advertisement {
	private String description;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private boolean isFulfilled;
	@ManyToMany
	private Set<Application> applications;

}
