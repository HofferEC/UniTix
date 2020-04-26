package us.wi.hofferec.unitix.adapters;

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

public class ProfileTicketsSellingAdapter extends RecyclerView.Adapter<ProfileTicketsSellingAdapter.TicketHolder> {

    private List<Ticket> tickets;

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    class TicketHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;
        public TextView infoTextView;
        public TextView dateTextView;
        public TextView priceTextView;
        public TextView status;
        public Button retractButton;

        /**
         * Constructor that accepts the entire item row and does the view lookups to find each
         * subview.
         *
         * @param itemView item row
         */
        public TicketHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.tv_details_profile_event);
            infoTextView = itemView.findViewById(R.id.tv_details_profile_info);
            dateTextView = itemView.findViewById(R.id.tv_details_profile_date);
            priceTextView = itemView.findViewById(R.id.tv_details_profile_price);
            status = itemView.findViewById(R.id.tv_details_profile_status);
            retractButton = itemView.findViewById(R.id.button_details_profile_retract);
        }
    }

    public ProfileTicketsSellingAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ProfileTicketsSellingAdapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_tickets_selling, parent, false);
        return new ProfileTicketsSellingAdapter.TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketHolder ticketHolder, final int position) {

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

        if (tickets.get(position).isAvailable()) {
            ticketHolder.status.setVisibility(View.GONE);
            ticketHolder.retractButton.setText("Retract");
            ticketHolder.retractButton.setOnClickListener(v -> {
                tickets.get(position).setAvailable(false);
                tickets.get(position).setRetracted(true);
                Utility.updateTicketOnDatabase("ProfileTicketsSellingAdapter", tickets.get(position));
            });
        } else {
            ticketHolder.retractButton.setVisibility(View.GONE);
            ticketHolder.status.setText(tickets.get(position).isRetracted() ? "Retracted" : "Sold");
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
