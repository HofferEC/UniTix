package us.wi.hofferec.unitix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.LoginActivity;
import us.wi.hofferec.unitix.data.Ticket;
import us.wi.hofferec.unitix.data.Utility;

public class ProfileTicketsBuyingAdapter extends RecyclerView.Adapter<ProfileTicketsBuyingAdapter.TicketHolder> {

    private List<Ticket> tickets;
    private Context context;

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    class TicketHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;
        public TextView infoTextView;
        public TextView dateTextView;
        public TextView priceTextView;
        public Button viewButton;

        /**
         * Constructor that accepts the entire item row and does the view lookups to find each
         * subview.
         *
         * @param itemView item row
         */
        public TicketHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.tv_details_event);
            infoTextView = itemView.findViewById(R.id.tv_details_info);
            dateTextView = itemView.findViewById(R.id.tv_details_date);
            priceTextView = itemView.findViewById(R.id.tv_details_price);
            viewButton = itemView.findViewById(R.id.action_button);
        }
    }

    public ProfileTicketsBuyingAdapter(List<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileTicketsBuyingAdapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new ProfileTicketsBuyingAdapter.TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketHolder ticketHolder, int position) {

        ticketHolder.eventTextView.setText(tickets.get(position).getEvent());

        StringBuilder info = new StringBuilder(tickets.get(position).getAwayTeam() + " @ " + tickets.get(position).getHomeTeam());
        ticketHolder.infoTextView.setText(info);

        ticketHolder.dateTextView.setText(tickets.get(position).getDate());

        StringBuilder price = new StringBuilder();

        if (LoginActivity.user.getSettings().get("currency").equals("USD")){
            price.append("$" + tickets.get(position).getPrice());
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("EUR")) {
            price.append("€" + Utility.convert(Double.parseDouble(tickets.get(position).getPrice()), "EUR"));
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("GBP")) {
            price.append("£" + Utility.convert(Double.parseDouble(tickets.get(position).getPrice()), "GBP"));
        }
        ticketHolder.priceTextView.setText(price);

        ticketHolder.viewButton.setText("View");
        ticketHolder.viewButton.setOnClickListener(v -> {
            showTicket(position);
        });
    }

    private void showTicket(int position) {
        Ticket t = tickets.get(position);
        String filepath = t.getTicketPath().replace("/tickets/", "");
        Utility.openTicket(context, "ProfileBuyingAdapter", filepath);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
