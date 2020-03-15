package us.wi.hofferec.unitix.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents the data model for a ticket.
 */
public class Ticket {

    private String date;
    private String homeTeam;
    private String awayTeam;
    private String event;
    private static int id;
    private boolean isAvailable;

    /**
     * Empty Constructor
     */
    public Ticket() {
    }

    /**
     * Constructor.
     *
     * @param date date to set
     * @param homeTeam homeTeam to set
     * @param awayTeam awayTeam to set
     * @param event event to set
     */
    public Ticket(String date, String homeTeam, String awayTeam, String event, boolean isAvailable) {
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.event = event;
        this.isAvailable = isAvailable;
        id++;
    }

    /**
     * Getter for isAvailable.
     *
     * @return current isAvailable
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Setter for isAvailable.
     *
     * @param available isAvailable to set
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Getter for id.
     *
     * @return current id
     */
    public static int getId() {
        return id;
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

        for(int i = 0 ; i < numTickets ; i++) {
            awayTeamRand = getRandomIntegerBetweenRange(0, teams.length - 1);
            homeTeamRand = getRandomIntegerBetweenRange(0, teams.length - 1);
            eventsRand = getRandomIntegerBetweenRange(0, events.length - 1);
            isAvailableRand = getRandomIntegerBetweenRange(0, 1);
            isAvailable = isAvailableRand == 1;
            Ticket ticket = new Ticket(date, teams[homeTeamRand], teams[awayTeamRand], events[eventsRand], isAvailable);
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
