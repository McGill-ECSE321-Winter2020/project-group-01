package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany
	@Getter
	@Setter
	private Set<User> subscribers;

	@OneToMany(mappedBy = "forum", cascade = { CascadeType.ALL })
	@Getter
	@Setter
	private Set<Comment> comments;

}
