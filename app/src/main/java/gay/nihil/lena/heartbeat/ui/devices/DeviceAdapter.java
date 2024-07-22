package gay.nihil.lena.heartbeat.ui.devices;

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

import gay.nihil.lena.heartbeat.User;
import gay.nihil.lena.heartbeat.R;
import gay.nihil.lena.heartbeat.User;
import gay.nihil.lena.heartbeat.Utils;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.UserViewHolder> {
    Context context;
    List<User> users;

    RecyclerView recyclerView;

    public DeviceAdapter(Context ctx, RecyclerView view){
        users = new ArrayList<User>();
        context = ctx;
        recyclerView = view;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_card, parent, false);


        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Bind data for the User item to the views in the UserViewHolder
        if (users == null) {
            return;
        }

        User user = users.get(position);

        holder.user.setText(user.name);
        // TODO: get user status directly from the service
        holder.status.setText("Unknown");
        holder.last_update.setText(Utils.timestampToText(user.last_active_time));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setItems(List<User> usr, boolean fullUpdate) {
        users = usr;

        if (fullUpdate) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(0);
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        TextView status;
        TextView last_update;

        UserViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.device_name);
            status = itemView.findViewById(R.id.device_status);
            last_update = itemView.findViewById(R.id.device_last_update);

            itemView.setOnLongClickListener(v -> {
                Snackbar.make(itemView, "This should open a menu to rename the device", Snackbar.LENGTH_LONG).show();
                return true;
            });
        }
    }
}