package ca.mcgill.ecse321.petshelter.service;

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
	public Comment addComment(String text, long forumID, long userID) {
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
					forumRepository.save(newForum);
					return newComment;
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
	 */
	@Transactional
	public Comment updateComment(long commentID, String comment) {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			Comment updatedComment = oldComment.get();
			updatedComment.setText(comment);
			commentRepository.save(updatedComment);
			return updatedComment;
		} else {
			throw new CommentException("No such comment.");
		}
	}
	
	/**
	 * Delete the comment.
	 *
	 * @param commentID The ID of the comment.
	 * @return The deleted comment.
	 */
	@Transactional
	public Comment deleteComment(long commentID) {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			commentRepository.deleteById(commentID);
			return oldComment.get();
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
	public List<Comment> getComments() {
		List<Comment> comments = commentRepository.findAll();
		return comments;
	}
	
	/**
	 * Get all the comments of a user.
	 *
	 * @param userID The id of the user.
	 * @return The list of all comments by the user.
	 */
	@Transactional
	public List<Comment> getCommentsByUser(long userID) {
		Optional<User> user = userRepository.findById(userID);
		if (user.isPresent()) {
			List<Comment> comments = commentRepository.findCommentsByUser(user.get());
			return comments;
		} else {
			throw new CommentException("No such user.");
		}
	}
	
}
