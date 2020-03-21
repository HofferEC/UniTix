package us.wi.hofferec.unitix.data;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the data model for a user.
 */
public class User {

    private String email;
    private String username;
    private List<DocumentReference> tickets;
    private HashMap<String, Object> settings;
    private String dateOfBirth;
    private String phone;
    private String profileImageUri;

    /**
     * Empty Constructor (Used for FireStore serialization).
     */
    public User() {
    }

    /**
     * Constructor used when a user signs up for the first time.
     *
     * @param email email
     */
    public User(String email, HashMap<String, Object> settings) {
        this.email = email;
        this.settings = settings;
    }

    /**
     * Constructor.
     *
     * @param email email
     * @param tickets associated tickets
     * @param dateOfBirth date of birth
     * @param phone phone
     * @param username username
     */
    public User(String email, List<DocumentReference> tickets, String dateOfBirth, String phone, String username, HashMap<String, Object> settings) {
        this.email = email;
        this.tickets = tickets;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.settings = settings;
    }

    /**
     * Getter for profileImageUUID.
     *
     * @return current profileImageUUID
     */
    public String getProfileImageUri() {
        return profileImageUri;
    }

    /**
     * Setter for profileImageUUID.
     *
     * @param profileImageUri profileImageUUID to set
     */
    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    /**
     * Getter for settings.
     *
     * @return current settings
     */
    public HashMap<String, Object> getSettings() {
        return settings;
    }

    /**
     * Setter for settings.
     *
     * @param settings settings to set
     */
    public void setSettings(HashMap<String, Object> settings) {
        this.settings = settings;
    }

    /**
     * Add a new setting to users settings
     *
     * @param setting name of the settings
     * @param value value associated with the setting
     */
    public void addSetting(String setting, Object value) {
        if (settings == null) {
            settings = new HashMap<>();
        }
        settings.put(setting, value);
    }

    /**
     * Getter for username.
     *
     * @return current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username.
     *
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for email
     *
     * @return current email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for tickets.
     *
     * @return current tickets
     */
    public List<DocumentReference> getTickets() {
        return tickets;
    }

    /**
     * Setter for tickets.
     *
     * @param tickets tickets to set
     */
    public void setTickets(List<DocumentReference> tickets) {
        this.tickets = tickets;
    }

    /**
     * Add a ticket to this users history.
     *
     * @param documentReference document reference of ticket
     */
    public void addTicket(DocumentReference documentReference) {
        if(tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(documentReference);
    }

    /**
     * Getter for dateOfBirth.
     *
     * @return current dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Setter for dateOfBirth.
     *
     * @param dateOfBirth dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Getter for phone.
     *
     * @return current phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for phone.
     *
     * @param phone phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
