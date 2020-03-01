package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.dto.UserDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.ForumException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.mcgill.ecse321.petshelter.service.UserService.userToDto;

/**
 * Services to handle the creation, modification and deletion of forum threads.
 *
 * @author mathieu
 */

@Service
public class ForumService {
	@Autowired
	private ForumRepository forumRepository;
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Add a forum to the database. The creator is the only subscriber.
	 *
	 * @param creator Author of the thread.
	 * @param title   Title of the thread.
	 * @return The forum that was added.
	 */
	@Transactional
	public ForumDTO addForum(String title, User creator) {
		Forum newForum = new Forum();
		newForum.setTitle(title);
		newForum.setAuthor(creator);
		Set<User> user = new HashSet<>();
		user.add(creator);
		newForum.setSubscribers(user);
		Set<Comment> comments = new HashSet<>();
		newForum.setComments(comments);
		forumRepository.save(newForum);
		return forumToDTO(newForum);
	}
	
	/**
	 * Delete the specified forum from the database.
	 *
	 * @param forumID The forum to delete.
	 * @return The forum that was deleted. This value may be null;
	 */
	@Transactional
	public void deleteForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			forumRepository.deleteById(forumID);
		}
		
	}
	
	/**
	 * Lock a forum.
	 *
	 * @param forumID The id of the forum to lock.
	 * @return The forum that was locked.
	 */
	@Transactional
	public ForumDTO lockForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setLocked(true);
			forumRepository.save(newForum);
			return forumToDTO(newForum);
		} else {
			throw new ForumException("No such forum thread.");
		}
		
	}
	
	/**
	 * Unlock a forum.
	 *
	 * @param forumID The id of the forum to unlock.
	 * @return The forum that was unlocked.
	 */
	@Transactional
	public ForumDTO unlockForum(long forumID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setLocked(false);
			forumRepository.save(newForum);
			return forumToDTO(newForum);
		} else {
			throw new ForumException("No such forum thread.");
		}
	}
	
	/**
	 * Subscribe a user to a forum thread.
	 *
	 * @param forumID The forum to subscribe the user to.
	 * @param userID  The user to subscribe to the thread.
	 * @return The forum to which the user was subscribed to.
	 */
	@Transactional
	public ForumDTO subscribeTo(long forumID, long userID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		Optional<User> user = userRepository.findById(userID);
		if (user.isPresent()) {
			if (forum.isPresent()) {
				Forum newForum = forum.get();
				Set<User> newUsers = newForum.getSubscribers();
				newUsers.add(user.get());
				newForum.setSubscribers(newUsers);
				forumRepository.save(newForum);
				return forumToDTO(newForum);
			} else {
				throw new ForumException("No such forum thread.");
			}
		} else {
			throw new ForumException("No such user.");
		}
	}
	
	/**
	 * Unsubscribe a user to a forum thread.
	 *
	 * @param forumID The forum to unsubscribe the user from.
	 * @param userID  The user to unsubscribe from the thread.
	 * @return The forum from which the user was unsubscribed.
	 */
	@Transactional
	public ForumDTO unsubscribeFrom(long forumID, long userID) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			Set<User> newUsers = newForum.getSubscribers();
			newUsers.removeIf(u -> (u.getId() == userID));
			newForum.setSubscribers(newUsers);
			forumRepository.save(newForum);
			return forumToDTO(newForum);
		} else {
			throw new ForumException("No such forum thread.");
		}
	}
	
	/**
	 * Update a forum title.
	 *
	 * @param forumID The id of the forum to lock.
	 * @param title   The updated title.
	 * @return The updated forum.
	 */
	@Transactional
	public ForumDTO updateForum(long forumID, String title) {
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (forum.isPresent()) {
			Forum newForum = forum.get();
			newForum.setTitle(title);
			forumRepository.save(newForum);
			return forumToDTO(newForum);
		} else {
			throw new ForumException("No such forum thread.");
		}
	}

	/**
	 * Get all the forums of a user.
	 *
	 * @param userID The id of the user.
	 * @return The list of all forums by the user.
	 */
	@Transactional
	public List<ForumDTO> getForumsByUser(long userID) {
		Optional<User> user = userRepository.findById(userID);
		if (user.isPresent()) {
			return forumRepository.findForumsByAuthor(user.get()).stream().map(this::forumToDTO).collect(Collectors.toList());
		} else {
			throw new ForumException("No such user.");
		}
	}

	/**
	 * Get the list of all the forum threads.
	 *
	 * @return List of all forum threads.
	 */
	@Transactional
	public List<ForumDTO> getAllForum() {
		return forumRepository.findAll().stream().map(this::forumToDTO).collect(Collectors.toList());
	}
	
	@Transactional
	public ForumDTO getForumWithID(Long id) {
		Optional<Forum> forum = forumRepository.findById(id);
		if (forum.isPresent()) {
			return forumToDTO(forum.get());
		} else {
			throw new ForumException("Forum not found");
		}
	}
	
	public ForumDTO forumToDTO(Forum forum) {
		ForumDTO forumDTO = new ForumDTO();
		forumDTO.setAuthor(userToDto(forum.getAuthor()));
		forumDTO.setTitle(forum.getTitle());
		forumDTO.setId(forum.getId());
		Set<UserDTO> forumSubsDTO = null;
		for (User user : forum.getSubscribers()){
			forumSubsDTO.add(userToDto(user));
		}
		forumDTO.setSubscribers(forumSubsDTO);
		return forumDTO;
	}
}
