package ca.mcgill.ecse321.petshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

/**
 * Services to handle the creation, modification and deletion of forum threads.
 * @author mathieu
 *
 */

@Service
public class ForumService {
	@Autowired
	private ForumRepository forumRepository;
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Add a forum to the database. The creator is the only subscriber.
	 * @param creator Author of the thread.
	 * @param title Title of the thread.
	 * @return The forum that was added.
	 */
	@Transactional
	public Forum addForum(String title, User creator) {
		Forum newForum = new Forum();
		newForum.setTitle(title);
		newForum.setAuthor(creator);
		Set<User> user = new HashSet<>();
		user.add(creator);
		newForum.setSubscribers(user);
		forumRepository.save(newForum);
		return newForum;
	}
	
	/**
	 * Delete the specified forum from the database.
	 * @param forumID The forum to delete.
	 * @return The forum that was deleted. This value may be null;
	 */
	@Transactional
	public void deleteForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if(forum.isPresent()) {
			forumRepository.deleteById(forumID);
		}
		
	}
	
	/**
	 * Lock a forum.
	 * @param forumID The id of the forum to lock.
	 * @return The forum that was locked.
	 */
	@Transactional
	public Forum lockForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setLocked(true);
			forumRepository.save(newForum);
			return newForum;
		} else {
			throw new ForumException("No such forum thread.");
		}
		
	}
	
	/**
	 * Unlock a forum.
	 * @param forumID The id of the forum to unlock.
	 * @return The forum that was unlocked.
	 */
	@Transactional
	public Forum unlockForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setLocked(false);
			forumRepository.save(newForum);
			return newForum;
		} else {
			throw new ForumException("No such forum thread.");
		}
	}
	
	/**
	 * Subscribe a user to a forum thread.
	 * @param forumID The forum to subscribe the user to.
	 * @param userID The user to subscribe to the thread.
	 * @return The forum to which the user was subscribed to.
	 */
	@Transactional
	public Forum subscribeTo(long forumID, long userID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		Optional<User> user = userRepository.findById(userID);
		if (user.isPresent()) {
			if (forum.isPresent()) {
				Forum newForum = forum.get();
				Set<User> newUsers = newForum.getSubscribers();
				newUsers.add(user.get());
				newForum.setSubscribers(newUsers);
				forumRepository.save(newForum);
				return newForum;
			} else {
				throw new ForumException("No such forum thread.");
			}
		} else {
			throw new ForumException("No such user.");
		}
	}
	
	/**
	 * Unsubscribe a user to a forum thread.
	 * @param forumID The forum to unsubscribe the user from.
	 * @param userID The user to unsubscribe from the thread.
	 * @return The forum from which the user was unsubscribed.
	 */
	@Transactional
	public Forum unsubscribeFrom(long forumID, long userID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			Set<User> newUsers = newForum.getSubscribers();
			newUsers.removeIf(u -> (u.getId() == userID));
			newForum.setSubscribers(newUsers);
			forumRepository.save(newForum);
			return newForum;
		} else {
			throw new ForumException("No such forum thread.");
		}
	}
	
	/**
	 * Update a forum title.
	 * @param forumID The id of the forum to lock.
	 * @param title The updated title.
	 * @return The updated forum.
	 */
	@Transactional
	public Forum updateForum(long forumID, String title) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setTitle(title);
			forumRepository.save(newForum);
			return newForum;
		} else {
			throw new ForumException("No such forum thread.");
		}
	}

	/**
	 * Get the list of all the forum threads.
	 * @return List of all forum threads.
	 */
	@Transactional
	public List<Forum> getForums() {
		List<Forum> forums = forumRepository.findAll();
		return forums;
	}
	
}
