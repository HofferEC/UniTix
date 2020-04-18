package us.wi.hofferec.unitix.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.MainActivity;
import us.wi.hofferec.unitix.data.Ticket;
import us.wi.hofferec.unitix.data.Utility;

import static us.wi.hofferec.unitix.helpers.App.CHANNEL_1_ID;

public class Notifications {

    // Initiates notification and marks the transaction as seen on the Firebase
    public static void notifyTicketIsSold(Context context, Ticket t){
        sendSoldNotification(context, t);
        t.setSeen(true);
        Utility.updateTicketOnDatabase("UTIL", t);
    }

    // Pushes notification to user that their ticket has been sold.
    private static void sendSoldNotification(Context context, Ticket ticket){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        String title = "A ticket you posted has been sold!";
        String message = "Event: " + ticket.getEvent() + "\n" +
                "Teams: " + ticket.getAwayTeam() + " @ " + ticket.getHomeTeam() + "\n" +
                "Date: " + ticket.getDate() + "\n" +
                "Price: $" + ticket.getPrice();

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);

        Notification notif = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_profile)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notif);
    }
}
