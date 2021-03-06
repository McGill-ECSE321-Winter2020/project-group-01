package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.CommentService;
import ca.mcgill.ecse321.petshelter.service.exception.CommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class implements the REST controller for the comment related features of the backend.
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentService commentService;
	
	/**
     * Get all the comments in the database and return them. Only the admin may get
     * those.
     *
     * @param threadID thread ID
     * @param token    admin's token
     * @return The list of all comments.
     */
	@GetMapping("/all/{threadID}")
	public ResponseEntity<?> getAllComments(@RequestHeader String token, @PathVariable long threadID) throws CommentException {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null && requester.getUserType().equals(UserType.ADMIN)) {
			Set<CommentDTO> comments = commentService.getComments(threadID);
			return new ResponseEntity<>(comments, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get all the comments of a user. Anybody can see anyone's comments.
	 *
	 * @param username The username of the user which is the author of all desired
	 *                 comments.
	 * @param token The user session access token.
	 *
	 * @return The list of all comments of a user.
	 */
	@GetMapping("/{username}")
	public ResponseEntity<?> getUserComments(@PathVariable String username, @RequestHeader String token) {
		User requester = userRepository.findUserByApiToken(token);
		if (requester != null) {
			try {
				List<CommentDTO> comments = commentService.getCommentsByUser(userRepository.findUserByUserName(username).getId());
				return new ResponseEntity<>(comments, HttpStatus.OK);
			} catch (CommentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * Create a comment on a designated forum thread.
	 *
	 * @param commentText The comment to add.
	 * @param id          The id of the forum thread to respond to.
	 * @param token       The user session token.
	 *
	 * @return The comment added to the thread.
	 */
	@PostMapping("/{id}")
	public ResponseEntity<?> createComment(@RequestBody String commentText, @PathVariable long id,
			@RequestHeader String token) {
		User user = userRepository.findUserByApiToken(token);
		// Check if the user exists.
		if (user != null) {
			try {
				return new ResponseEntity<>(commentService.addComment(commentText, id, user.getId()), HttpStatus.CREATED);
			} catch (CommentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update a comment.
	 *
	 * @param commentDTO The update to the existing comment.
	 * @param id         The forum thread id (it is not actually used).
	 * @param token      The user session token.
	 * @param commentId  The comment id.
	 *
	 * @return The modified comment.
	 */
	@PutMapping("/{id}/{commentId}")
	public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO, @PathVariable long id,
			@RequestHeader String token, @PathVariable long commentId) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Comment> oldComment = commentRepository.findById(commentId);
		// if the user updating the comment is the comment's author.
		if (oldComment.isPresent() && user != null
				&& user.getUserName().equals(oldComment.get().getUser().getUserName())) {
			try {
				return new ResponseEntity<>(commentService.updateComment(commentId, commentDTO.getText()), HttpStatus.OK);
			} catch (CommentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete a comment from a forum thread.
	 *
	 * @param token     The user session token.
	 * @param commentId The id of the comment.
	 * @param id The forum id. (It is not actually used).
	 *
	 * @return The deleted comment.
	 */
	@DeleteMapping("/{id}/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable long id, @RequestHeader String token,
			@PathVariable long commentId) {
		User user = userRepository.findUserByApiToken(token);
		Optional<Comment> oldComment = commentRepository.findById(commentId);
		// if the user updating the comment is the comment's author or an admin
		if (oldComment.isPresent() && user != null
				&& (user.getUserName().equals(oldComment.get().getUser().getUserName())
						|| user.getUserType().equals(UserType.ADMIN))) {
			try {
				commentService.deleteComment(commentId);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (CommentException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
