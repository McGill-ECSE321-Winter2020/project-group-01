package ca.mcgill.ecse321.petshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.Forum;
import ca.mcgill.ecse321.petshelter.model.User;
import ca.mcgill.ecse321.petshelter.repository.CommentRepository;
import ca.mcgill.ecse321.petshelter.repository.ForumRepository;
import ca.mcgill.ecse321.petshelter.repository.UserRepository;

/**
 * Service for interacting with comments.
 * @author mathieu
 *
 */

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ForumRepository forumRepository;
	
	/**
	 * Add a comment to a thread.
	 * @param text Comment text.
	 * @param forumID ID of the commented thread.
	 * @param userID ID of the author of the comment.
	 * @return The created comment.
	 */
	
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
					newComment.setUser(user.get());
					Set<Comment> comments = newForum.getComments();
					comments.add(newComment);
					newForum.setComments(comments);
					forumRepository.save(newForum);
					return newComment;
				} else {
					throw new IllegalStateException("Forum thread is locked.");
				}
			} else {
				throw new NullPointerException("No such forum thread.");
			}
		} else {
			throw new NullPointerException("No such user.");
		}
	}
	
	/**
	 * Update a comment.
	 * @param commentID Comment ID.
	 * @param comment Comment update.
	 * @return
	 */
	
	public Comment updateComment(long commentID, String comment) {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			Comment updatedComment = oldComment.get();
			updatedComment.setText(comment);
			commentRepository.save(updatedComment);
			return updatedComment;
		} else {
			throw new NullPointerException("No such comment.");
		}
	}
	
	/**
	 * Delete the comment.
	 * @param commentID The ID of the comment.
	 * @return The deleted comment.
	 */
	
	public Comment deleteComment(long commentID) {
		Optional<Comment> oldComment = commentRepository.findById(commentID);
		if (oldComment.isPresent()) {
			commentRepository.deleteById(commentID);
			return oldComment.get();
		} else {
			throw new NullPointerException("No such comment.");
		}
	}
	
	/**
	 * Get the list of all comments.
	 * @return The list of all comments.
	 */
	
	public List<Comment> getComments() {
		List<Comment> comments = commentRepository.findAll();
		return comments;
	}

}
