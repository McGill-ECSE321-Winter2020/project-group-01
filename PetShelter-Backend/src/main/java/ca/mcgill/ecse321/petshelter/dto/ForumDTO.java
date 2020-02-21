package ca.mcgill.ecse321.petshelter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.sql.Time;

public class ForumDTO {
	
	@NotNull(message = "Title cannot be empty.")
	@NotEmpty(message = "Title cannot be empty.")
	private String title;
		
	public ForumDTO() {
	}
	
	public ForumDTO(@NotNull @NotEmpty String title) {
		this.title = title;
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
}
