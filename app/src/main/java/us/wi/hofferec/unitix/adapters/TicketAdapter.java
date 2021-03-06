package us.wi.hofferec.unitix.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import us.wi.hofferec.unitix.R;
import us.wi.hofferec.unitix.activities.ConfirmPurchaseActivity;
import us.wi.hofferec.unitix.activities.LoginActivity;
import us.wi.hofferec.unitix.data.Ticket;
import us.wi.hofferec.unitix.data.Utility;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {

    private List<Ticket> ticketsList;

    /**
     * Provides a direct reference to each of the views within a data item and caches the views
     * within the item layout for fast access.
     */
    class TicketHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;
        public TextView infoTextView;
        public TextView dateTextView;
        public TextView priceTextView;
        public Button buyButton;

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
            buyButton = itemView.findViewById(R.id.action_button);
        }
    }

    public TicketAdapter(List<Ticket> tickets) {
        this.ticketsList = tickets;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder ticketHolder, int position) {

        final Ticket ticket = ticketsList.get(position);

        ticketHolder.eventTextView.setText(ticket.getEvent());

        StringBuilder info = new StringBuilder(ticket.getAwayTeam() + "\n@ " + ticket.getHomeTeam());
        ticketHolder.infoTextView.setText(info);

        ticketHolder.dateTextView.setText(ticket.getDate());

        StringBuilder price = new StringBuilder();

        if (LoginActivity.user.getSettings().get("currency").equals("USD")){
            price.append("$" + ticket.getPrice());
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("EUR")) {
            price.append("€" + Utility.convert(Double.parseDouble(ticket.getPrice()), "EUR"));
        }
        else if (LoginActivity.user.getSettings().get("currency").equals("GBP")) {
            price.append("£" + Utility.convert(Double.parseDouble(ticket.getPrice()), "GBP"));
        }
        ticketHolder.priceTextView.setText(price);

        ticketHolder.buyButton.setText(ticket.isAvailable() ? "Buy" : "Unavailable");
        ticketHolder.buyButton.setEnabled(ticket.isAvailable());

        ticketHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ConfirmPurchaseActivity.class);
                intent.putExtra("ticket", ticket);
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketHolder(view);
    }

    @Override
    public int getItemCount() {
        return ticketsList.size();
    }

}
