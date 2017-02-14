package de.tobiasbielefeld.solitaire.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import de.tobiasbielefeld.solitaire.R;
import de.tobiasbielefeld.solitaire.ui.Main;

/**
 * Created by phzhou on 12/5/16.
 */


public class RestartDialog extends DialogFragment {

    public interface RestartDialogListener {
        void onFinishedRestartDialog(int result);
    }

    private RestartDialogListener listener;

    public static final int NEW_GAME = 0;
    public static final int RE_DEAL = 1;
    public static final int CANCEL = 2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (RestartDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement RestartDialogListener");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.app_name)
                .setItems(R.array.restart_menu, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // "which" argument contains index of selected item. 0 is new game, 1 is re-deal
                        // game.newGame(which);
                        listener.onFinishedRestartDialog(which);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //just cancel
                    }
                });

        return builder.create();
    }
}
