package ca.mcgill.ecse321.petshelter.controller;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.model.UserType;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	 * Convert a comment entity to a comment DTO.
	 *
	 * @param comment The comment to convert.
	 * @return A comment DTO.
	 */
	public static CommentDTO commentToDto(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setDatePosted(comment.getDatePosted());
		commentDTO.setId(comment.getId());
		commentDTO.setText(comment.getText());
		commentDTO.setTime(comment.getTime());
		commentDTO.setUsername(comment.getUser().getUserName());
		return commentDTO;
	}
	
	/**
	 * Get all the comments in the database and return them.
	 *
	 * @return The list of all comments.
	 */
	@GetMapping("/all")
	public List<CommentDTO> getAllComments() {
		return commentService.getComments().stream().map(CommentController::commentToDto).collect(Collectors.toList());
	}
	
	/**
	 * Get all the comments of a user.
	 *
	 * @param username The username of the user which is the author of all desired comments.
	 * @return The list of all comments of a user.
	 */
	@GetMapping("/{username}")
	public List<CommentDTO> getUserComments(@PathVariable String username) {
		return commentService.getCommentsByUser(userRepository.findUserByUserName(username).getId()).stream()
				.map(CommentController::commentToDto).collect(Collectors.toList());
	}
	
	/**
	 * Create a comment on a designated forum thread.
	 *
	 * @param commentDTO The comment DTO to add.
	 * @param id         The id of the forum thread to respond to.
	 * @param token      The user session token.
	 * @return The comment added to the thread.
	 */
	@PostMapping("/{id}")
	public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO, @PathVariable long id,
										   @RequestHeader String token) {
		System.out.println(commentDTO.toString());
		System.out.println("Thread ID=" + id);
		User user = userRepository.findUserByApiToken(token);
		// Check if the user posting the comment is actually the declared used.
		if (user.getUserName().equals(commentDTO.getUsername())) {
			Comment commentCreated = commentService.addComment(commentDTO.getText(), id, user.getId());
			System.out.println(commentCreated.toString());
			return new ResponseEntity<>(commentToDto(commentCreated), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Update a comment.
	 *
	 * @param commentDTO The update to the existing comment.
	 * @param id         The forum thread id.
	 * @param token      The user session token.
	 * @param commentId  The comment id.
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
			Comment comment = commentService.updateComment(commentId, commentDTO.getText());
			return new ResponseEntity<>(commentToDto(comment), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Delete a comment from a forum thread.
	 *
	 * @param token     The user session token.
	 * @param commentId The id of the comment.
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
			commentService.deleteComment(commentId);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
