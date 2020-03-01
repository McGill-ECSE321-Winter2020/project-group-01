package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.ForumDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/forum")
public class ForumController {

	@Autowired
	ForumRepository forumRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ForumService forumService;

	/**
	 * Gets the desired forum and its associated comments.
	 *
	 * @param id Forum ID of the desired forum.
	 * @return The forum DTO.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getForum(@PathVariable(required = true) Long id, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		ForumDTO forumDTO = forumService.getForumWithID(id);
		if (forum.isPresent() && requester != null) {
			return new ResponseEntity<>(forumDTO, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get all the forum threads of a user.
	 *
	 * @param username The username of the user which is the author of all desired
	 *                 comments.
	 * @return The list of all forums of a user.
	 */
	@GetMapping("/forums/{username}")

	public ResponseEntity<?> getUserForums(@PathVariable String username, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			List<ForumDTO> forums = forumService.getForumsByUser(userRepository.findUserByUserName(username).getId());
			return new ResponseEntity<>(forums, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Gets all existing forums thread.
	 *
	 * @return List of all existing forum threads.
	 */
	@GetMapping("/all")

	public ResponseEntity<?> getAllForums(@RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if(requester == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(forumService.getAllForum(), HttpStatus.OK);

	}

	/**
	 * Create new forum thread.
	 *
	 * @param title The title of the forum to create.
	 * @param token The session token of the user.
	 * @return The created forum.
	 */
	@PostMapping()
	public ResponseEntity<?> createForum(@RequestBody String title, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		if (user != null && title != null && !title.trim().equals("")) {
			return new ResponseEntity<>(forumService.addForum(title, user), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update the title of a forum thread.
	 *
	 * @param forumId The id of a given forum.
	 * @param title   The new title of the forum.
	 * @param token   The session token of the user.
	 * @return The modified forum.
	 */
	@PutMapping("/{forumId}")
	public ResponseEntity<?> updateForum(@PathVariable long forumId, @RequestBody String title,
			@RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Forum> oldForum = forumRepository.findById(forumId);
		if (user != null && oldForum.isPresent() // Verify the forum already exists.
				&& oldForum.get().getAuthor().getId() == user.getId() // Check if the issuing user is the author.
				&& title != null // Check if the new title is valid.
				&& !title.trim().equals("")) {
			ForumDTO forum = forumService.updateForum(forumId, title);
			return new ResponseEntity<ForumDTO>(forum, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Lock or unlock a given forum. By design, only admin are allowed to lock and
	 * unlock forums.
	 *
	 * @param forumID  The id of a given forum.
	 * @param token    The session token of a user.
	 * @param isLocked The status to set the forum thread to.
	 * @return The modified forum.
	 */
	@PutMapping("/lock/{forumID}")
	public ResponseEntity<?> setLockForum(@PathVariable long forumID, @RequestHeader String token,
			@RequestBody Boolean isLocked) {
		User user = userRepository.findUserByApiToken(token);
		if (user != null && user.getUserType() == UserType.ADMIN) {
			ForumDTO forum;
			if (isLocked) {
				forum = forumService.lockForum(forumID);
			} else {
				forum = forumService.unlockForum(forumID);
			}
			return new ResponseEntity<ForumDTO>(forum, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete a forum thread from the database. By design, only an admin may delete
	 * a forum thread.
	 *
	 * @param forumID Forum id of the forum to delete.
	 * @param token   Session token of the user.
	 * @return The deleted forum.
	 */
	@DeleteMapping("/{forumID}")
	public ResponseEntity<?> deleteForum(@PathVariable long forumID, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (user != null && forum.isPresent() && user.getUserType().equals(UserType.ADMIN)) {
			// delete all comments

			Set<Comment> comments = forum.get().getComments();
			for (Iterator<Comment> it = comments.iterator(); it.hasNext();) {
				it.next();
				it.remove();
			}
			forumRepository.delete(forum.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Subscribe to a forum.
	 *
	 * @param forumID The id of the forum.
	 * @param token   The session token of the user.
	 * @return The modify forum thread.
	 */
	@PutMapping("/subscribe/{forumID}")
	public ResponseEntity<?> subscribeToForum(@PathVariable long forumID, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (user != null && forum.isPresent()) {
			ForumDTO newForum = forumService.subscribeTo(forumID, user.getId());
			return new ResponseEntity<ForumDTO>(newForum, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Unsubscribe to from a forum.
	 *
	 * @param forumID The id of the forum.
	 * @param token   The session token of the user.
	 * @return The modify forum thread.
	 */
	@PutMapping("/unsubscribe/{forumID}")
	public ResponseEntity<?> unsubscribeFromForum(@PathVariable long forumID, @RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (user != null && forum.isPresent()) {
			ForumDTO newForum = forumService.unsubscribeFrom(forumID, user.getId());
			return new ResponseEntity<ForumDTO>(newForum, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
