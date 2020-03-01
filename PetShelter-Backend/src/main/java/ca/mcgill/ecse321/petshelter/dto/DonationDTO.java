package ca.mcgill.ecse321.petshelter.dto;

import java.sql.Date;
import java.sql.Time;


public class DonationDTO {
    private String username;
    private Date date;
    private Time time;
    private Double amount;
    private String email;
    
    //Empty constructor
    public DonationDTO() {
    }
    
    public DonationDTO(String username, Date date, Time time, Double amount) {
        this.username = username;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUser(String username) {
        this.username = username;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "DonationDTO{" +
                "user=" + username +
                ", date=" + date +
                ", time=" + time +
                ", amount=" + amount +
                '}';
    }
}
