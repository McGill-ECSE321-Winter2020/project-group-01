package ca.mcgill.ecse321.petshelter.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.petshelter.model.Advertisement;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Donation;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.Gender;
import ca.mcgill.ecse321.petshelter.model.Pet;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;

@Repository
public class PetShelterRepository {

	@Autowired
	EntityManager entityManager;

	// Advertisement

	@Transactional
	public Advertisement createAdvertisement() {
		Advertisement a = new Advertisement();
		entityManager.persist(a);
		return a;
	}

	@Transactional
	public Advertisement getAdvertisement(long id) {
		Advertisement a = entityManager.find(Advertisement.class, id);
		return a;
	}

	// Comment

	@Transactional
	public Comment createComment() {
		Comment c = new Comment();
		entityManager.persist(c);
		return c;
	}

	@Transactional
	public Comment getComment(long id) {
		Comment c = entityManager.find(Comment.class, id);
		return c;
	}

	// Donation

	@Transactional
	public Donation createDonation() {
		Donation d = new Donation();
		entityManager.persist(d);
		return d;
	}

	@Transactional
	public Donation getDonation(long id) {
		Donation d = entityManager.find(Donation.class, id);
		return d;
	}

	// Forum

	@Transactional
	public Forum createForum() {
		Forum f = new Forum();
		entityManager.persist(f);
		return f;
	}

	@Transactional
	public Forum getForum(long id) {
		Forum f = entityManager.find(Forum.class, id);
		return f;
	}

	// Pet

	@Transactional
	public Pet createPet() {
		Pet p = new Pet();
		entityManager.persist(p);
		return p;
	}

	@Transactional
	public Pet getPet(long id) {
		Pet p = entityManager.find(Pet.class, id);
		return p;
	}

	// User

	@Transactional
	public User createUser() {
		User u = new User();
		entityManager.persist(u);
		return u;
	}

	@Transactional
	public User getUser(long id) {
		User u = entityManager.find(User.class, id);
		return u;
	}

}