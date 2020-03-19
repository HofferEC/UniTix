package us.wi.hofferec.unitix.data;

import java.util.List;

public class User {

    private String email;
    private String username;
    private List<String> tickets;
    private String dateOfBirth;
    private String phone;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, List<String> tickets, String dateOfBirth, String phone, String username) {
        this.email = email;
        this.tickets = tickets;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(String ticketId) {
        tickets.add(ticketId);
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
