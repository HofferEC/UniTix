package us.wi.hofferec.unitix.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the data model for a ticket.
 */
public class Ticket implements Serializable {

    private String date;
    private String homeTeam;
    private String awayTeam;
    private String event;
    private String price;
    private boolean available;
    private boolean retracted;
    private String uid;
    private String ticketPath;
    private String seller;

    /**
     * Empty Constructor (Used for FireStore serialization)
     */
    public Ticket() {
    }

    /**
     * Constructor.
     *
     * @param date date of the event
     * @param homeTeam home team
     * @param awayTeam away team
     * @param event event
     * @param available is the ticket available
     * @param price price
     */
    public Ticket(String date, String homeTeam, String awayTeam, String event, boolean available, boolean retracted, String price, String uid, String seller) {
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.event = event;
        this.available = available;
        this.retracted = retracted;
        this.price = price;
        this.uid = uid;
        this.seller = seller;
    }




    /**
     * Getter for ticketPath.
     *
     * @return current ticketPath
     */
    public String getTicketPath() {
        return ticketPath;
    }

    /**
     * Setter for ticketPath.
     * @param ticketPath ticketPath to set
     */
    public void setTicketPath(String ticketPath) {
        this.ticketPath = ticketPath;
    }

    /**
     * Getter for uid.
     *
     * @return current uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter for uid.
     * @param uid uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter for price.
     *
     * @return current price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Setter for price.
     *
     * @param price price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Getter for available.
     *
     * @return current available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Setter for available.
     *
     * @param available available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Getter for retracted.
     *
     * @return current retracted
     */
    public boolean isRetracted() {
        return retracted;
    }

    /**
     * Setter for retracted.
     *
     * @param retracted retracted to set
     */
    public void setRetracted(boolean retracted) {
        this.retracted = retracted;
    }

    /**
     * Getter for date.
     *
     * @return current date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for date.
     *
     * @param date date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for homeTeam.
     *
     * @return current homeTeam
     */
    public String getHomeTeam() {
        return homeTeam;
    }

    /**
     * Setter for homeTeam.
     *
     * @param homeTeam homeTeam to set
     */
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    /**
     * Getter for awayTeam.
     *
     * @return current awayTeam
     */
    public String getAwayTeam() {
        return awayTeam;
    }

    /**
     * Setter for awayTeam.
     *
     * @param awayTeam awayTeam to set
     */
    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    /**
     * Getter for event.
     *
     * @return current event
     */
    public String getEvent() {
        return event;
    }

    /**
     * Setter for event.
     *
     * @param event event to set
     */
    public void setEvent(String event) {
        this.event = event;
    }

    public void setSeller(String seller) {this.seller = seller;}
    public String getSeller() {return this.seller;}

    /**
     * Will simulate a list of tickets
     *
     * For testing only
     *
     * @param numTickets number of tickets to simulate
     * @return list of random tickets
     */
    public static ArrayList<Ticket> getTickets(int numTickets) {
        ArrayList<Ticket> tickets = new ArrayList<>();

        String[] teams = {"Wisconsin", "Michigan", "Ohio State", "Nebraska", "Iowa", "Rutgers", "Indiana", "Michigan State", "Maryland", "Penn State", "Minnesota", "Purdue"};
        String[] events = {"Basketball", "Baseball", "Football", "Soccer", "Tennis", "Bowling"};

        int awayTeamRand;
        int homeTeamRand;
        int eventsRand;
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date today = new Date();
        String date = dateFormat.format(today);
        double isAvailableRand;
        boolean isAvailable;
        boolean isRetracted;
        String price = "10.35";
        String uid = "0";
        String seller = null;

        for(int i = 0 ; i < numTickets ; i++) {
            awayTeamRand = getRandomIntegerBetweenRange(0, teams.length - 1);
            homeTeamRand = getRandomIntegerBetweenRange(0, teams.length - 1);
            eventsRand = getRandomIntegerBetweenRange(0, events.length - 1);
            isAvailableRand = getRandomIntegerBetweenRange(0, 1);
            isAvailable = isAvailableRand == 1;
            isRetracted = false;
            Ticket ticket = new Ticket(date, teams[homeTeamRand], teams[awayTeamRand], events[eventsRand], isAvailable, isRetracted, price, uid, seller);
            tickets.add(ticket);
        }

        return tickets;
    }

    /**
     * Helper method for random generator.
     *
     * @param min min number
     * @param max max number
     * @return random number between min and max
     */
    private static int getRandomIntegerBetweenRange(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return (int)x;
    }
}
