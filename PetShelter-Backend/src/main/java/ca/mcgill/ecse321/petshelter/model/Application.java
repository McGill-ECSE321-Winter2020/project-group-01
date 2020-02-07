
package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author louis
 *
 */
@Entity
@Data
@Table(name = "Application")
public class Application {
	private String description;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private boolean isAccepted;
	
	@OneToOne(optional = false)
	private User applicant;
	
	@OneToOne(optional = false)
	private Advertisement advertisement;

}