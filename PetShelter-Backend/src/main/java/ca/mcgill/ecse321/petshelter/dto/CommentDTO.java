package ca.mcgill.ecse321.petshelter.dto;

import java.sql.Date;

public class CommentDTO {
	private Date datePosted;
	
	private long id;
	private String text;
	/**
	 * Author of the comment.
	 */
	
	private UserDTO user;
	
	public Date getDatePosted() {
		return this.datePosted;
	}
	
	public void setDatePosted(Date value) {
		this.datePosted = value;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long value) {
		this.id = value;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String value) {
		this.text = value;
	}
	
	/**
	 * Return the author of the comment.
	 *
	 * @return The author.
	 */
	
	public UserDTO getUser() {
		return this.user;
	}
	
	/**
	 * Set the author of the comment.
	 *
	 * @param user The new author.
	 */
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "Comment{" +
				"datePosted=" + datePosted +
				", id=" + id +
				", text='" + text + '\'' +
				", user=" + user +
				'}';
	}
}
