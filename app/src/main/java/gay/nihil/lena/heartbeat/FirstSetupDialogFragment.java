package gay.nihil.lena.heartbeat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

public class FirstSetupDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.first_run_dialog, null);

        EditText hostname = dialogView.findViewById(R.id.first_run_host);

        builder.setView(dialogView)
                .setCancelable(false)
                .setNeutralButton(R.string.i_dont_have_a_server, (dialog, which) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.git_url)));
                    startActivity(browserIntent);
                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (!hostname.getText().toString().isEmpty()) {
                        PreferenceManager
                                .getDefaultSharedPreferences(this.getContext())
                                .edit()
                                .putString("server", hostname.getText()
                                        .toString())
                                .apply();
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }

}
