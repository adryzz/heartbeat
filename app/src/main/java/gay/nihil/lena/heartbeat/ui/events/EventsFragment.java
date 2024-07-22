package gay.nihil.lena.heartbeat.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gay.nihil.lena.heartbeat.Event;
import gay.nihil.lena.heartbeat.MainActivity;
import gay.nihil.lena.heartbeat.User;
import gay.nihil.lena.heartbeat.databinding.FragmentEventsBinding;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.eventsView;
        onConnection((MainActivity)getActivity());
        return root;
    }

    public void onConnection(MainActivity act) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LiveData<List<Event>> events = act.service.getEvents();
        LiveData<List<User>> users = act.service.getUsers();
        EventAdapter adapter = new EventAdapter(getContext(), recyclerView);

        users.observe(act, usr -> {
            adapter.setUsers(usr, false);
        });

        events.observe(act, evt -> {
            adapter.setItems(evt, false);
            recyclerView.smoothScrollToPosition(0);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}