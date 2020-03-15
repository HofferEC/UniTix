package us.wi.hofferec.unitix.data;

import java.util.Date;

/**
 * Represents the data model for a ticket.
 */
public class Ticket {

    private Date date;
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
    public Ticket(Date date, String homeTeam, String awayTeam, String event, boolean isAvailable) {
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
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     *
     * @param date date to set
     */
    public void setDate(Date date) {
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
}
