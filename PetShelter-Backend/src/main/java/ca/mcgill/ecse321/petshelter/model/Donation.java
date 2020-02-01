package ca.mcgill.ecse321.petshelter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import ca.mcgill.ecse321.petshelter.model.java.sql.Date;
import javax.persistence.ManyToOne;
import ca.mcgill.ecse321.petshelter.model.java.sql.Time;

@Entity
public class Donation{
   private double amount;

public void setAmount(double value) {
    this.amount = value;
}
public double getAmount() {
    return this.amount;
}
private long idDonation;

public void setIdDonation(long value) {
    this.idDonation = value;
}
@Id
public long getIdDonation() {
    return this.idDonation;
}
private Date yearOfDonation;

public void setYearOfDonation(Date value) {
    this.yearOfDonation = value;
}
public Date getYearOfDonation() {
    return this.yearOfDonation;
}
private Date monthOfDonation;

public void setMonthOfDonation(Date value) {
    this.monthOfDonation = value;
}
public Date getMonthOfDonation() {
    return this.monthOfDonation;
}
private Date dayOfDonation;

public void setDayOfDonation(Date value) {
    this.dayOfDonation = value;
}
public Date getDayOfDonation() {
    return this.dayOfDonation;
}
private User user;

@ManyToOne(optional=false)
public User getUser() {
   return this.user;
}

public void setUser(User user) {
   this.user = user;
}

private Time timeOfDonation;

public void setTimeOfDonation(Time value) {
    this.timeOfDonation = value;
}
public Time getTimeOfDonation() {
    return this.timeOfDonation;
}
}
