package gay.nihil.lena.heartbeat.ui.events;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import gay.nihil.lena.heartbeat.Event;
import gay.nihil.lena.heartbeat.R;
import gay.nihil.lena.heartbeat.User;
import gay.nihil.lena.heartbeat.Utils;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    List<Event> events;
    Context context;
    List<User> users;

    RecyclerView recyclerView;
    long lastTime = Long.MAX_VALUE;
    int lastFlags = Integer.MAX_VALUE;

    public EventAdapter(Context ctx, RecyclerView view){
        events = new ArrayList<Event>();
        context = ctx;
        recyclerView = view;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);


        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Bind data for the Event item to the views in the EventViewHolder
        if (events == null || users == null) {
            return;
        }

        Event event = events.get(position);
        User user = users.stream().filter((u) -> u.id == event.user)
                .findFirst().orElse(Utils.getPlaceholderUser());

        holder.user.setText(user.name);
        holder.text.setText(Utils.flagsToText(event.flags));
        holder.timestamp.setText(Utils.timestampToText(event.timestamp));

        lastTime = event.timestamp;
        lastFlags = event.flags;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setItems(List<Event> evt, boolean fullUpdate) {
        events = evt;

        if (fullUpdate) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(0);
        }
    }

    public void setUsers(List<User> usr, boolean fullUpdate) {
        users = usr;

        if (fullUpdate) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(0);
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        TextView text;
        TextView timestamp;

        EventViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.name_text);
            text = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.timestamp_text);

            itemView.setOnLongClickListener(v -> {
                Snackbar.make(itemView, "This should open a menu or something idk", Snackbar.LENGTH_LONG).show();
                return true;
            });
        }
    }
}