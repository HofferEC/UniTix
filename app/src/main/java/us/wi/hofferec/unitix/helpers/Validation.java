package us.wi.hofferec.unitix.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Validation {

    // Validates each string of the Ticket listing. Returns false if at least one field is invalid
    public static boolean validateTicket(Context context, String date, String event, String home,
                                         String away, String price, String path){
        if (!validateDate(date)){
            Log.e("VALIDATION", "Date invalid: " + date);
            Toast.makeText(context, "Please enter a valid date\nFormat: mm/dd/yyyy", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validateStrings(event)){
            Log.e("VALIDATION", "Event invalid: " + event);
            Toast.makeText(context, "Please enter a valid event\nMust contain 1-64 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validateStrings(home)){
            Log.e("VALIDATION", "Home Team invalid: " + home);
            Toast.makeText(context, "Please enter a valid Home Team\nMust contain 1-64 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validateStrings(away)){
            Log.e("VALIDATION", "Away Team invalid: " + away);
            Toast.makeText(context, "Please enter a valid Away Team\nMust contain 1-64 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validatePrice(price)){
            Log.e("VALIDATION", "Price invalid: " + price);
            Toast.makeText(context, "Please enter a valid price\nBetween .01-9999", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validatePath(path)){
            Log.e("VALIDATION", "File invalid: " + path);
            Toast.makeText(context, "Please enter a valid ticket file.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // Input validation for a date string
    private static boolean validateDate(String date){
        String [] parts = date.split("/");
        if (parts.length != 3) return false;
        try {
            int day = Integer.parseInt(parts[1]);
            if (parts[1].length() != 2 || day < 0 || day > 31) return false;
            int month = Integer.parseInt(parts[0]);
            if (parts[0].length() != 2 || month < 0 || month > 12) return false;
            int year = Integer.parseInt(parts[2]);
            if (parts[2].length() != 4 || year < 2020) return false;
        } catch (Exception e){
            return false;
        }
        return true;
    }

    // Input validation for the strings (i.e. Home Team, Away Team, Events)
    private static boolean validateStrings(String event) {
        return (event.length() > 0 && event.length() < 64);
    }

    // Input validation for the price field.
    private static boolean validatePrice(String priceString) {
        try {
            float price = Float.parseFloat(priceString);
            if (price < .01 || price > 9999)
                return false;
            if (priceString.contains(".") && priceString.indexOf(".") < priceString.length() - 3)
                return false;
        } catch (Exception e){
            return false;
        }
        return true;
    }

    // Input validation for the path
    private static boolean validatePath(String path) {
        return (path != null && path.length() > 0);
    }
}
