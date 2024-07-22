package gay.nihil.lena.heartbeat.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gay.nihil.lena.heartbeat.Event;
import gay.nihil.lena.heartbeat.MainActivity;
import gay.nihil.lena.heartbeat.User;
import gay.nihil.lena.heartbeat.databinding.FragmentDevicesBinding;
import gay.nihil.lena.heartbeat.ui.devices.DeviceAdapter;

public class DevicesFragment extends Fragment {

    private FragmentDevicesBinding binding;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.devicesView;
        onConnection((MainActivity)getActivity());
        return root;
    }

    public void onConnection(MainActivity act) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LiveData<List<User>> users = act.service.getUsers();
        DeviceAdapter adapter = new DeviceAdapter(getContext(), recyclerView);

        users.observe(act, usr -> {
            adapter.setItems(usr, false);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}