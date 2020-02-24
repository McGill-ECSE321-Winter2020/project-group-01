package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.petshelter.model.Comment;
import ca.mcgill.ecse321.petshelter.model.User;

import java.util.Set;

public class ForumDTO {
	
	@NotNull(message = "Title cannot be empty.")
	@NotEmpty(message = "Title cannot be empty.")
	private String title;
	
	@NotNull(message = "A forum must have at least one comment.")
	@NotEmpty(message = "A forum must have at least one comment.")
	private Set<Comment> comments;
	
	private Set<User> subscribers;
		
	public ForumDTO() {
	}
	
	public ForumDTO(@NotNull @NotEmpty String title, @NotNull @NotEmpty Set<Comment> comments, Set<User> subscribers) {
		this.title = title;
		this.comments = comments;
		this.subscribers = subscribers;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the comments
	 */
	public Set<Comment> getComments() {
		return comments;
	}
	
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	/**
	 * @return the subscribers
	 */
	public Set<User> getSubscribers() {
		return subscribers;
	}
	
	/**
	 * @param subscribers the subscribers to set
	 */
	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}
}
