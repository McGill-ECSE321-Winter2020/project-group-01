import javax.persistence.Entity;

@Entity
public enum StaffOrCustomer{
   private String staff;

public void setStaff(String value) {
    this.staff = value;
}
public String getStaff() {
    return this.staff;
}
private String customer;

public void setCustomer(String value) {
    this.customer = value;
}
public String getCustomer() {
    return this.customer;
}
}
