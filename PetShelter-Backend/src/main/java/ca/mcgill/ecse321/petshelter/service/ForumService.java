package ca.mcgill.ecse321.petshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	 * Add a forum to the database.
	 * @param forum Forum to add.
	 * @return The forum that was added.
	 */
	@Transactional
	public Forum addForum(String title) {
		Forum newForum = new Forum();
		newForum.setTitle(title);
		newForum.setLocked(false);
		forumRepository.save(newForum);
		return newForum;
	}
	
	/**
	 * Delete the specified forum from the database.
	 * @param forumID The forum to delete.
	 * @return The forum that was deleted. This value may be null;
	 */
	@Transactional
	public Forum deleteForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if(forum.isPresent()) {
			forumRepository.deleteById(forumID);
			return forum.get();
		} else {
			throw new NullPointerException("No such forum thread.");
		}
		
	}
	
	/**
	 * Lock a forum.
	 * @param The id of the forum to lock.
	 * @return The forum that was locked.
	 */
	@Transactional
	public Forum lockForum(long forumID) {
		if (forumRepository.existsById(forumID)) {
			Forum forum = forumRepository.getOne(forumID);
			forum.setLocked(true);
			forumRepository.save(forum);
			return forum;
		} else {
			throw new NullPointerException("No such forum thread.");
		}
		
	}
	
	/**
	 * Unlock a forum.
	 * @param The id of the forum to unlock.
	 * @return The forum that was unlocked.
	 */
	@Transactional
	public Forum unlockForum(long forumID) {
		if (forumRepository.existsById(forumID)) {
			Forum forum = forumRepository.getOne(forumID);
			forum.setLocked(false);
			forumRepository.save(forum);
			return forum;
		} else {
			throw new NullPointerException("No such forum thread.");
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
		if (userRepository.existsById(userID)) {
			if (forumRepository.existsById(forumID)) {
				Forum forum = forumRepository.findForumById(forumID);
				Set<User> newUsers = forum.getSubscribers();
				newUsers.add(userRepository.findUserById(userID));
				forum.setSubscribers(newUsers);
				forumRepository.save(forum);
				return forum;
			} else {
				throw new NullPointerException("No such forum thread.");
			}
		} else {
			throw new NullPointerException("No such user.");
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
		if (userRepository.existsById(userID)) {
			if (forumRepository.existsById(forumID)) {
				Forum forum = forumRepository.findForumById(forumID);
				Set<User> newUsers = forum.getSubscribers();
				newUsers.removeIf(u -> (u.getId() == userID));
				forum.setSubscribers(newUsers);
				forumRepository.save(forum);
				return forum;
			} else {
				throw new NullPointerException("No such forum thread.");
			}
		} else {
			throw new NullPointerException("No such user.");
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
