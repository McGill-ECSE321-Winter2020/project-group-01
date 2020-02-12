package ca.mcgill.ecse321.petshelter.dto;

import ca.mcgill.ecse321.petshelter.model.User;

import java.sql.Date;
import java.sql.Time;

//Todo: DTO part 2.8.2-2

public class DonationDTO {
    private User user;
    private Date date;
    private Time time;
    
    //Empty constructor
    public DonationDTO() {
    }
    
    public DonationDTO(User user, Date date, Time time) {
        this.user = user;
        this.date = date;
        this.time = time;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Time getTime() {
        return time;
    }
    
    public void setTime(Time time) {
        this.time = time;
    }
    
}
