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
    private List<DocumentReference> ticketsSelling;
    private List<DocumentReference> ticketsBuying;
    private HashMap<String, Object> settings;
    private String dateOfBirth;
    private String phone;
    private String profileImageUri;
    private String token;

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
    public User(String email, HashMap<String, Object> settings, String username) {
        this.email = email;
        this.settings = settings;
        this.username = username;
    }

    /**
     * Constructor.
     *  @param email email
     * @param ticketsSelling tickets being sold
     * @param ticketsBuying tickets being bought
     * @param dateOfBirth date of birth
     * @param phone phone
     * @param username username
     */
    public User(String email, List<DocumentReference> ticketsSelling, List<DocumentReference> ticketsBuying, String dateOfBirth, String phone, String username, HashMap<String, Object> settings, String token) {
        this.email = email;
        this.ticketsSelling = ticketsSelling;
        this.ticketsBuying = ticketsBuying;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.settings = settings;
        this.token = token;
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
     * Getter for ticketsBuying.
     *
     * @return current ticketsBuying
     */
    public List<DocumentReference> getTicketsBuying() {
        if (ticketsBuying == null)
            ticketsBuying = new ArrayList<>();
        return ticketsBuying;
    }

    /**
     * Setter for ticketBuying.
     *
     * @param ticketsBuying ticketsBuying to set
     */
    public void setTicketsBuying(List<DocumentReference> ticketsBuying) {
        if (ticketsBuying == null) {
            ticketsBuying = new ArrayList<>();
        }
        this.ticketsBuying = ticketsBuying;
    }

    /**
     * Getter for ticketsSelling.
     *
     * @return current ticketsSelling
     */
    public List<DocumentReference> getTicketsSelling() {
        if (ticketsSelling == null)
            ticketsSelling = new ArrayList<>();

        return ticketsSelling;
    }

    /**
     * Setter for ticketsSelling.
     *
     * @param ticketsSelling ticketsSelling to set
     */
    public void setTicketsSelling(List<DocumentReference> ticketsSelling) {
        if (ticketsSelling == null)
            ticketsSelling = new ArrayList<>();
        this.ticketsSelling = ticketsSelling;
    }

    public String getToken() { return this.token; }

    public void setToken(String token) { this.token = token; }

    /**
     * Add a ticket to this users selling history.
     *
     * @param documentReference document reference of ticket
     */
    public void addTicketToSelling(DocumentReference documentReference) {
        if(ticketsSelling == null) {
            ticketsSelling = new ArrayList<>();
        }
        ticketsSelling.add(documentReference);
    }

    /**
     * Add a ticket to this users buying history.
     *
     * @param documentReference document reference of ticket
     */
    public void addTicketToBuying(DocumentReference documentReference) {
        if(ticketsBuying == null) {
            ticketsBuying = new ArrayList<>();
        }
        ticketsBuying.add(documentReference);
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
