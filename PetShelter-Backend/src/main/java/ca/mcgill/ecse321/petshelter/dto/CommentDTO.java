package ca.mcgill.ecse321.petshelter.dto;

import java.sql.Date;
import java.sql.Time;

public class CommentDTO {
	private Date datePosted;
	
	private long id;
	private String text;
	/**
	 * Author of the comment.
	 */
	
	private String username;

	private Time time;

	public void setTime(Time value) {
		this.time = value;
	}

	public Time getTime() {
		return this.time;
	}
	
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
	
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Set the author of the comment.
	 *
	 * @param username The new author.
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "CommentDTO {" +
				"datePosted=" + datePosted +
				", id=" + id +
				", text='" + text + '\'' +
				", username=" + username +
				", time=" + time +
				'}';
	}
}
