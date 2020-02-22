package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.mcgill.ecse321.petshelter.model.Comment;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

public class ForumDTO {
	
	@NotNull(message = "Title cannot be empty.")
	@NotEmpty(message = "Title cannot be empty.")
	private String title;
	
	@NotNull(message = "A forum must have at least one comment.")
	@NotEmpty(message = "A forum must have at least one comment.")
	private Set<Comment> comments;
	
	private Set<String> subscribers;
		
	public ForumDTO() {
	}
	
	public ForumDTO(@NotNull @NotEmpty String title, @NotNull @NotEmpty Set<Comment> comments, Set<String> subscribers) {
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
	public Set<String> getSubscribers() {
		return subscribers;
	}
	
	/**
	 * @param subscribers the subscribers to set
	 */
	public void setSubscribers(Set<String> subscribers) {
		this.subscribers = subscribers;
	}
}
