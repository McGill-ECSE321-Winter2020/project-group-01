package ca.mcgill.ecse321.petshelter.service;

import ca.mcgill.ecse321.petshelter.dto.CommentDTO;
import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;
import ca.mcgill.ecse321.petshelter.service.exception.CommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for interacting with comments.
 *
 * @author mathieu
 */

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ForumRepository forumRepository;
	
	/*
	some stuff doenst work:
	1- comments seems to be erased after
	2- multiple comments are not saved, even with different names
	 */
	
	/**
	 * Add a comment to a thread.
	 *
	 * @param text    Comment text.
	 * @param forumID ID of the commented thread.
	 * @param userID  ID of the author of the comment.
	 * @return The created comment.
	 */
	@Transactional
	public CommentDTO addComment(String text, long forumID, long userID) throws CommentException {
		Optional<User> user = userRepository.findById(userID);
		Optional<Forum> forum = forumRepository.findById(forumID);
		if (user.isPresent()) {
			if (forum.isPresent()) {
				Forum newForum = forum.get();
				if (!newForum.isLocked()) {
					Comment newComment = new Comment();
					newComment.setText(text);
					newComment.setDatePosted(new Date(System.currentTimeMillis()));
					newComment.setTime(new Time(System.currentTimeMillis()));
					newComment.setUser(user.get());
					Set<Comment> comments = newForum.getComments();
					comments.add(newComment);
					newForum.setComments(comments);
					commentRepository.save(newComment);
					forumRepository.save(newForum);
					return commentToDto(newComment);
				} else {
					throw new CommentException("Forum thread is locked.");
				}
			} else {
				throw new CommentException("No such forum thread.");
			}
		} else {
			throw new CommentException("No such user.");
		}
	}
	
	/**
	 * Update a comment.
	 *
	 * @param commentID Comment ID.
	 * @param comment   Comment update.
	 * @return The updated comment.
	 * @throws CommentException If the user doesn't exists.
	 */
	@Transactional
	public CommentDTO updateComment(long commentID, String comment) throws CommentException {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			Comment updatedComment = oldComment.get();
			updatedComment.setText(comment);
			commentRepository.save(updatedComment);
			return commentToDto(updatedComment);
		} else {
			throw new CommentException("No such comment.");
		}
	}
	
	/**
	 * Delete the comment.
	 *
	 * @param commentID The ID of the comment.
	 * @return The deleted comment.
	 * @throws CommentException If the user doesn't exists.
	 */
	@Transactional
	public CommentDTO deleteComment(long commentID) throws CommentException {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			commentRepository.deleteById(commentID);
			return commentToDto(oldComment.get());
		} else {
			throw new CommentException("No such comment.");
		}
	}
	
	/**
	 * Get the list of all comments.
	 *
	 * @return The list of all comments.
	 */
	@Transactional
	public List<CommentDTO> getComments() {
		return commentRepository.findAll().stream()
				.map(CommentService::commentToDto)
				.collect(Collectors.toList());
	}
	
	/**
	 * Get all the comments of a user.
	 *
	 * @param userID The id of the user.
	 * @return The list of all comments by the user.
	 */
	@Transactional
	public List<CommentDTO> getCommentsByUser(long userID) throws CommentException {
		Optional<User> user = userRepository.findById(userID);
		if (user.isPresent()) {
			return commentRepository.findCommentsByUser(user.get()).stream()
					.map(CommentService::commentToDto)
					.collect(Collectors.toList());
		} else {
			throw new CommentException("No such user.");
		}
	}
	
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
	
}
